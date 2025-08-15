import React, { useState } from 'react';
import './PaymentForm.css';

const generateYears = () => {
  const currentYear = new Date().getFullYear();
  return Array.from({ length: 10 }, (_, i) => currentYear - 5 + i);
};

const PaymentForm = () => {
  const [formData, setFormData] = useState({
    studyYear: '',
    studyTerm: '',
    admissionNumber: '',
    feePaid: '',
    referenceNumber: '',
    paymentDate: '',
    netBalance: ''
  });

  const [classEnrolled, setClassEnrolled] = useState('--');
  const [stream, setStream] = useState('--');
  const [feeItems, setFeeItems] = useState([]);
  const [payments, setPayments] = useState([]);
  const [feeTotals, setFeeTotals] = useState({ once: 0, annually: 0, everyTerm: 0 });
  const [oldBalance, setOldBalance] = useState(0); // ✅ Added

  const handleChange = (field, value) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }));

    if (field === 'feePaid') {
      calculateNetBalance(value);
    }
  };

  const fetchStudentData = () => {
    const { admissionNumber, studyYear, studyTerm } = formData;

    if (admissionNumber.length >= 3 && studyYear && studyTerm) {
      fetch(`http://localhost:8080/api/payments/load/${admissionNumber}/${studyYear}/${studyTerm}`)
        .then(res => {
          if (!res.ok) throw new Error("No student found");
          return res.json();
        })
        .then(data => {
          setClassEnrolled(data?.classEnrolled ?? '--');
          setStream(data?.stream ?? '--');

          const fees = Array.isArray(data?.feeItems) ? data.feeItems : [];
          const pays = Array.isArray(data?.payments) ? data.payments : [];
          setFeeItems(fees);
          setPayments(pays);

          let once = 0, annually = 0, everyTerm = 0;
          for (const item of fees) {
            const amt = item.amount ?? 0;
            const remark = item.remarks?.toLowerCase() ?? '';
            if (remark.includes("once")) once += amt;
            else if (remark.includes("annually")) annually += amt;
            else if (remark.includes("every term")) everyTerm += amt;
          }
          setFeeTotals({ once, annually, everyTerm });

          // ✅ Balance calculation synced with backend logic
          let computedOldBal = 0;
          if (pays.length === 0) {
            computedOldBal = once + annually + everyTerm;
          } else {
            const latestPayment = pays.reduce((a, b) =>
              (a.paymentid ?? 0) > (b.paymentid ?? 0) ? a : b
            );
            const lastBal = latestPayment?.balance ?? 0;
            const lastYear = latestPayment?.studyyear;
            const lastTerm = latestPayment?.term;

            if (lastYear !== studyYear) {
              computedOldBal = lastBal + annually + everyTerm;
            } else if (lastTerm !== studyTerm) {
              computedOldBal = lastBal + everyTerm;
            } else {
              computedOldBal = lastBal;
            }
          }

          setOldBalance(computedOldBal);

          setFormData(prev => ({
            ...prev,
            feePaid: '',
            netBalance: ''
          }));
        })
        .catch(err => {
          alert("Could not fetch student data. Please check the admission number and try again.");
          console.error('Failed to fetch:', err);
          setClassEnrolled('--');
          setStream('--');
          setFeeItems([]);
          setPayments([]);
        });
    }
  };

 const calculateNetBalance = (enteredAmount) => {
  const paid = parseFloat(enteredAmount);
  if (isNaN(paid)) {
    setFormData(prev => ({ ...prev, netBalance: '' }));
    return;
  }

  const { once, annually, everyTerm } = feeTotals;
  const { studyYear, studyTerm } = formData;

  let localOldBalance = 0;

  if (payments.length === 0) {
    localOldBalance = once + annually + everyTerm;
  } else {
    const latestPayment = payments.reduce((a, b) =>
      (a.paymentid ?? 0) > (b.paymentid ?? 0) ? a : b
    );
    const lastBal = latestPayment?.balance ?? 0;
    const lastYear = latestPayment?.studyyear;
    const lastTerm = latestPayment?.term;

    if (lastYear !== studyYear) {
      localOldBalance = lastBal + annually + everyTerm;
    } else if (lastTerm !== studyTerm) {
      localOldBalance = lastBal + everyTerm;
    } else {
      localOldBalance = lastBal;
    }
  }

  const net = localOldBalance - paid;
  setFormData(prev => ({ ...prev, netBalance: net.toFixed(2) }));
};


  const handleKeyDown = (e, fieldName) => {
    if (e.key === 'Enter') {
      const form = e.target.form;
      if (!form || !form.elements) return;

      const elements = Array.from(form.elements).filter(el => el.tagName === 'INPUT' || el.tagName === 'SELECT');
      const index = elements.indexOf(e.target);
      if (index >= 0 && index + 1 < elements.length) {
        elements[index + 1].focus();
        e.preventDefault();
      }

      if (fieldName === 'admissionNumber') {
        fetchStudentData();
      }
    }
  };

  const handleSavePayment = () => {
    if (!formData.admissionNumber || !formData.feePaid || !formData.paymentDate) {
      alert("Please fill in admission number, amount paid and payment date.");
      return;
    }

    const payload = {
      studentid: formData.admissionNumber,
      studentname: "Unknown",
      term: formData.studyTerm,
      studyyear: formData.studyYear,
      amount: parseFloat(formData.feePaid),
      paydate: formData.paymentDate,
      references: formData.referenceNumber
    };

    fetch("http://localhost:8080/payments/save", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(payload)
    })
      .then(res => {
        if (!res.ok) {
          return res.json().then(err => { throw new Error(err?.error || "Unknown error") });
        }
        return res.json();
      })
      .then(data => {
        alert("✅ Payment saved!");
        setFormData(prev => ({
          ...prev,
          feePaid: '',
          referenceNumber: '',
          paymentDate: '',
          netBalance: ''
        }));
        fetchStudentData();
      })
      .catch(err => {
        console.error("❌ Failed to save payment:", err);
        alert("Failed to save payment. " + err.message);
      });
  };

  const renderFormInputs = () => (
    <form className="form-section" onSubmit={e => e.preventDefault()}>
      <div className="form-group">
        <label>Study Year:</label>
        <select
          value={formData.studyYear}
          onChange={e => handleChange('studyYear', e.target.value)}
          onKeyDown={e => handleKeyDown(e)}
        >
          <option>-- Year --</option>
          {generateYears().map(year => (
            <option key={year} value={year}>{year}</option>
          ))}
        </select>
      </div>

      <div className="form-group">
        <label>Study Term:</label>
        <select
          value={formData.studyTerm}
          onChange={e => handleChange('studyTerm', e.target.value)}
          onKeyDown={e => handleKeyDown(e)}
        >
          <option>-- Term --</option>
          <option>Term1</option>
          <option>Term2</option>
          <option>Term3</option>
        </select>
      </div>

      <div className="form-group">
        <label>Enter Adm No:</label>
        <input
          type="text"
          value={formData.admissionNumber}
          onChange={e => handleChange('admissionNumber', e.target.value)}
          onKeyDown={e => handleKeyDown(e, 'admissionNumber')}
        />
      </div>

      <div className="form-group">
        <label>Fee Paid:</label>
        <input
          type="number"
          value={formData.feePaid}
          onChange={e => handleChange('feePaid', e.target.value)}
          onKeyDown={handleKeyDown}
        />
      </div>

      <div className="form-group">
        <label>Enter Ref No:</label>
        <input
          type="text"
          value={formData.referenceNumber}
          onChange={e => handleChange('referenceNumber', e.target.value)}
          onKeyDown={handleKeyDown}
        />
      </div>

      <div className="form-group">
        <label>Payment Date:</label>
        <input
          type="date"
          value={formData.paymentDate}
          onChange={e => handleChange('paymentDate', e.target.value)}
          onKeyDown={handleKeyDown}
        />
      </div>

      <div className="form-group">
        <label>NET Bal:</label>
        <input
          type="number"
          value={formData.netBalance}
          readOnly
        />
      </div>

      <div className="form-group">
        <button type="button" onClick={handleSavePayment}>💾 Save Payment</button>
      </div>
    </form>
  );

  return (
    <div className="payment-form-container">
      <h2>📋 School Fee Payments</h2>
      <table className="payment-table">
        <thead>
          <tr>
            <th>Details</th>
            <th>Class</th>
            <th>Stream</th>
            <th>Fee-Items</th>
            <th>Amount</th>
            <th>Remarks</th>
            <th colSpan="3">Payments Statement</th>
          </tr>
          <tr className="sub-header">
            <th></th><th></th><th></th><th></th><th></th><th></th>
            <th>Amount</th><th>Date</th><th>After-Pay Bal</th>
          </tr>
        </thead>
        <tbody>
          {(feeItems.length > 0 || payments.length > 0) ? (
            feeItems.map((item, index) => {
              const isTotalRow = item.remarks?.startsWith('<TOTAL:');
              const remarkText = isTotalRow
                ? item.remarks.replace('<TOTAL:', '').replace('>', '')
                : (item.remarks || '--');

              return (
                <tr key={index} className={`data-row ${isTotalRow ? 'remarks-total' : ''}`}>
                  {index === 0 && (
                    <td rowSpan={feeItems.length} className="details-column">
                      {renderFormInputs()}
                    </td>
                  )}
                  <td className="dotted-right centered-cell">{classEnrolled}</td>
                  <td className="centered-cell">{stream}</td>
                  <td className="dotted-double-left centered-cell">{item.itemDescription || '--'}</td>
                  <td className="dotted-double-center centered-cell">{item.amount ?? 0}</td>
                  <td className="centered-cell">{remarkText}</td>
                  <td className="centered-cell">{payments[index]?.amount || '--'}</td>
                  <td className="centered-cell">{payments[index]?.paydate || '--'}</td>
                  <td className="centered-cell">{payments[index]?.balance || '--'}</td>
                </tr>
              );
            })
          ) : (
            <tr className="data-row">
              <td className="details-column">{renderFormInputs()}</td>
              <td className="dotted-right centered-cell">--</td>
              <td className="centered-cell">--</td>
              <td className="dotted-double-left centered-cell">--</td>
              <td className="dotted-double-center centered-cell">--</td>
              <td className="centered-cell">--</td>
              <td className="centered-cell">--</td>
              <td className="centered-cell">--</td>
              <td className="centered-cell">--</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default PaymentForm;
