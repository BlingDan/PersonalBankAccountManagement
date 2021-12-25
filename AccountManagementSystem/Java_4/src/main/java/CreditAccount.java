class CreditAccount extends Account {
    private
    Accumulator acc;	//辅助计算利息的累加器
    double credit;		//信用额度
    double rate;		//欠款的日利率
    double fee;			//信用卡年费

    double getDebt()  {	//获得欠款额
        double balance = getBalance();
        return (balance < 0 ? balance : 0);
    }
    public
    //构造函数
    CreditAccount( Date date,  String id, double credit, double rate, double fee){
        super(date, id);
        this.credit = credit;
        this.rate = rate;
        this.fee = fee;
        this.acc = new Accumulator(date, 0);
    }
    double getCredit()  { return credit; }
    double getRate()  { return rate; }
    double getFee()  { return fee; }
    double getAvailableCredit()  {	//获得可用信用
        if (getBalance() < 0)
            return credit + getBalance();
        else
            return credit;
    }
    //存入现金
    void deposit( Date date, double amount,  String desc){
        record(date, amount, desc);
        acc.change(date, getDebt());
    }
    //取出现金
    void withdraw( Date date, double amount,  String desc){
        if (amount - getBalance() > credit) {
            error("not enough credit");
        } else {
            record(date, -amount, desc);
            acc.change(date, getDebt());
        }
    }
    //结算利息和年费，每月1日调用一次该函数
    void settle( Date date) {
        double interest = acc.getSum(date) * rate;
        if (interest != 0)
            record(date, interest, "interest");
        if (date.getMonth() == 1)
            record(date, -fee, "annual fee");
        acc.reset(date, getDebt());
    }

    void show() {
        super.show();
        System.out.print("\tAvailable credit:" + getAvailableCredit());
    }
}
