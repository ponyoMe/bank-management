async function loadOverview() {
    try {
        const response = await fetch("/reports/overview");
        const data = await response.json();

        document.getElementById("totalDeposits").textContent = `$${data.totalDeposits}`;
        document.getElementById("totalWithdrawals").textContent = `$${data.totalWithdrawals}`;
        document.getElementById("totalTransactions").textContent = data.totalTransactions;

        let recentTransactionsList = document.getElementById("recentTransactions");
        recentTransactionsList.innerHTML = ""; // Clear previous data

        data.recentTransactions.forEach(tx => {
            let li = document.createElement("li");
            let transactionType = tx.fromAccount == null ? "Deposit" :
                tx.toAccount == null ? "Withdrawal" :
                    "Transfer";
            li.innerHTML = `<strong>${transactionType}:</strong> $${tx.amount} - ${new Date(tx.createdAt).toLocaleString()}`;
            recentTransactionsList.appendChild(li);
        });
    } catch (error) {
        console.error("Error loading transaction overview:", error);
    }
}

window.onload = loadOverview;