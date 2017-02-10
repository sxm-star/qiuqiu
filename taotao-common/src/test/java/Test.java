
public class Test {

	public static void main(String[] args) {

		String hql = "select new com.dsd.subject.model.vo.InvestRecordReturn("
				+ "ir.id,u.loginAccount,ui.userTrueName,ir.investAmount,"
				+ "ir.investProfitAmount,ir.investTime,ir.investStatus,ir.investWay,s.subjectName,ir.repayedProfitAmount"
				+ ") from InvestRecord ir,User u,UserInfo ui,Subject s where 1=1 "
				+ "and ir.subjectId=s.id and ir.userLoginAccountId=ui.id and ir.userLoginAccountId=u.id and ir.investType=0";
		System.out.println(hql);
	}

}
