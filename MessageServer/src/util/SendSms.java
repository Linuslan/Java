package util;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
 
import util.DateStrFormat;

// ���ŷ����� 
public class SendSms {
	public static void main(String[] args) throws ParseException {	
		/*String strMobile="";
		String str="���������Ե�";		
		Send(strMobile,str);
		
		String strMobile="13459193542, 13665056709";
		String str="���������Ե�_List";	
		//Send(strMobile,str);
		List list = new ArrayList();
		list.add("13459193542");
		list.add("13459193542");
		list.add("13459193542");
		list.add("13459193542");
		list.add("13459193542");
		list.add("13459193542");
		list.add("13459193542");
		list.add("13459193542");
		list.add("13459193542");
		list.add("13459193542");
		list.add("13459193542");
		list.add("13459193542");
		list.add("13459193542");
		//list.add("13665056709");
		SendList(list, str);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("13459193542", "Map���ϲ���1");
		map.put("13459193542", "Map���ϲ���2");
		map.put("13459193542", "Map���ϲ���3");
		
		SendMap(map);*/ 
		SendSms.Send("13950209106", "����!����,2014��08��09������ֵ��,��������8��15��׼ʱ�Ӱ�,лл�������ݹ�����");
	}	 
	
	/**
	 * �ж����ƶ�����ͨ�����ŵĵ绰����
	 * @return
	 */
	 public static HashMap mobileHm(){			
		 HashMap hm=new HashMap();		
		 hm.put("134","0");
		 hm.put("135","0");
		 hm.put("136","0");
		 hm.put("137","0");
		 hm.put("138","0");
		 hm.put("139","0");
		 hm.put("147","0");
		 hm.put("150","0");
		 hm.put("151","0");
		 hm.put("152","0");
		 hm.put("157","0");
		 hm.put("158","0");
		 hm.put("159","0");
		 hm.put("182","0");
		 hm.put("187","0");
		 hm.put("188","0");
		
		 hm.put("130","1");
		 hm.put("131","1");
		 hm.put("132","1");
		 hm.put("155","1");
		 hm.put("156","1");
		 hm.put("185","1");
		 hm.put("186","1");
		 
		 hm.put("133","2");
		 hm.put("153","2");
		 hm.put("180","2");
		 hm.put("189","2");
		 return hm;
	 }
	public static String Send(String sDstPhone, String sMsgText) {
		String result = "";
		try {
			String endpoint = "http://10.130.36.87:8081/usp/services/InnerSysPublishInfoServiceFacadeImpl?wsdl";
			String userName = "000007", password = "000007",spNo="12110101203";
			// ֱ������Զ�̵�wsdl�ļ�			
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endpoint));
			call.setOperationName(new QName(endpoint, "login"));			
			String rcode = "";			
			String strSubMobile=sDstPhone.substring(0,3);		
			String strMType=mobileHm().get(strSubMobile)==null?"0":mobileHm().get(strSubMobile).toString();			
			String[][] str=new String[1][2];	
			str[0][0]="1";
			//�ֻ�����|�ط���|��Ӫ�̴���|��������
			str[0][1]=sDstPhone+"|"+spNo+"|"+strMType+"|"+sMsgText;		
			
			try {
				rcode = (String) call
						.invoke(new Object[] { userName, password });
				if (rcode.length() == 0) {
					System.out.println("---��¼���̽ӿ�ʧ��,������:" + rcode);
					return "";
				}
			} catch (Exception e) {
				System.out.println("---��¼���̽ӿ�ʧ��");
			}
			try {
				call.setOperationName(new QName(endpoint, "publishMessage"));
				call.setTargetEndpointAddress(new URL(endpoint));
				
				result = (String) call.invoke(new Object[] { userName,
						new String(rcode), spNo, str});
				if(result.trim().equals("000")){
					System.out.println("���ͳɹ���");
				}else{
					System.out.println("����ʧ�ܣ�");
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
			// ���������ݲ��������ҵ��÷���
			System.out.println("result is " + result);
		} catch (Exception e) {
			System.err.println(e.toString());

		}
		return result;
	}
	 
	/**
	 * �������ͣ�List��ʽ��
	 * @param sDstPhoneList
	 * @param sMsgText
	 * @return
	 */
	public static String SendList(List sDstPhoneList, String sMsgText) {
		
		String result = "";

		String endpoint = "http://10.130.36.87:8081/usp/services/InnerSysPublishInfoServiceFacadeImpl?wsdl";
		String userName = "000007", password = "000007",spNo="12110101203";
		
		try {
			

			String[][] array = new String[sDstPhoneList.size()][2];
			if(sDstPhoneList != null)
			{
				for(int i=0; i<sDstPhoneList.size(); i++)
				{
					String sDstPhone = sDstPhoneList.get(i).toString();
					String strSubMobile = sDstPhone.substring(0,10);
					String strMType=mobileHm().get(strSubMobile)==null?"0":mobileHm().get(strSubMobile).toString();
					array[i][0]= String.valueOf((i+1));
					//�ֻ�����|�ط���|��Ӫ�̴���|��������
					array[i][1]=sDstPhone+"|"+spNo+"|"+strMType+"|"+sMsgText;	
				}

				Service service = new Service();
				Call call = (Call) service.createCall();
				call.setTargetEndpointAddress(new URL(endpoint));
				call.setOperationName(new QName(endpoint, "login"));
				String rcode = "";	
				try {		
					rcode = (String) call
							.invoke(new Object[] { userName, password });
					if (rcode.length() == 0) {
						System.out.println("---��¼���̽ӿ�ʧ��,������:" + rcode);
						return "";
					}
				} catch (Exception e) {
					System.out.println("---��¼���̽ӿ�ʧ��");
				}
				
				try {
					call.setOperationName(new QName(endpoint, "publishMessage"));
					call.setTargetEndpointAddress(new URL(endpoint));
					result = (String) call.invoke(new Object[] { userName,
							new String(rcode), spNo, array});
					
					if(result.trim().equals("000")){
						System.out.println("���ͳɹ�!");
					}else{
						System.out.println("����ʧ��!");
					}
				} catch (Exception e) {
					System.err.println(e.toString());
				}
				
				// ���������ݲ��������ҵ��÷���
				System.out.println("result is " + result); 
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * �������ͣ�Map��ʽ��
	 * @param sDstPhoneList
	 * @param sMsgText
	 * @return
	 */
	public static String SendMap(Map<String, String> hmSendes) {
		
		String result = "";

		String endpoint = "http://10.130.36.87:8081/usp/services/InnerSysPublishInfoServiceFacadeImpl?wsdl";
		String userName = "000007", password = "000007",spNo="12110101203";
		
		try {
			

			String[][] array = new String[hmSendes.size()][2];
			if(hmSendes != null)
			{
				int i=0;
				for(String key : hmSendes.keySet())
				{
					String sDstPhone = key.toString().trim();
					String strSubMobile = sDstPhone.substring(0,10);
					String strMType=mobileHm().get(strSubMobile)==null?"0":mobileHm().get(strSubMobile).toString();
					array[i][0]= String.valueOf((i+1));
					//�ֻ�����|�ط���|��Ӫ�̴���|��������
					array[i][1]=sDstPhone+"|"+spNo+"|"+strMType+"|"+ hmSendes.get(key);
					i++;
				}
				 
				Service service = new Service();
				Call call = (Call) service.createCall();
				call.setTargetEndpointAddress(new URL(endpoint));
				call.setOperationName(new QName(endpoint, "login"));
				String rcode = "";	
				try {		
					rcode = (String) call
							.invoke(new Object[] { userName, password });
					if (rcode.length() == 0) {
						System.out.println("---��¼���̽ӿ�ʧ��,������:" + rcode);
						return "";
					}
				} catch (Exception e) {
					System.out.println("---��¼���̽ӿ�ʧ��");
				}
				
				try {
					call.setOperationName(new QName(endpoint, "publishMessage"));
					call.setTargetEndpointAddress(new URL(endpoint));
					result = (String) call.invoke(new Object[] { userName,
							new String(rcode), spNo, array});
					
					if(result.trim().equals("000")){
						System.out.println("���ͳɹ�!");
					}else{
						System.out.println("����ʧ��!");
					}
				} catch (Exception e) {
					System.err.println(e.toString());
				}
				
				// ���������ݲ��������ҵ��÷���
				System.out.println("result is " + result); 
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	private static String sendMess(String result, String endpoint,
			String userName, String password, String spNo, String[][] array)
			throws ServiceException, MalformedURLException {
		
		// ֱ������Զ�̵�wsdl�ļ�	
		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new URL(endpoint));
		call.setOperationName(new QName(endpoint, "login"));
		String rcode = "";		
		
		try {		
			rcode = (String) call
					.invoke(new Object[] { userName, password });
			if (rcode.length() == 0) {
				System.out.println("---��¼���̽ӿ�ʧ��,������:" + rcode);
				return "";
			}
		} catch (Exception e) {
			System.out.println("---��¼���̽ӿ�ʧ��");
		}
		try {
			call.setOperationName(new QName(endpoint, "publishMessage"));
			call.setTargetEndpointAddress(new URL(endpoint));
						
			result = (String) call.invoke(new Object[] { userName,
					new String(rcode), spNo, array});
			
			if(result.trim().equals("000")){
				System.out.println("���ͳɹ���");
			}else{
				System.out.println("����ʧ�ܣ�");
			}
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		return result;
	}
	
	/*public static ResponseBaseBean Send_old(String sDstPhone, String sMsgText) {
		SMSoapServiceServiceLocator loc = new SMSoapServiceServiceLocator();
		ResponseBaseBean resp = new ResponseBaseBean();
		try {
			SMSoapServiceSoapBindingStub sms_stub = new SMSoapServiceSoapBindingStub(
					new URL(loc.getSMSoapServiceAddress()), null);
			LogonInfo log = new LogonInfo();
			log.setUserName("jjwz");
			log.setPassWord("111111");
			resp = sms_stub.logon(log);
			if (resp.getErrorCode() != 0) {
				resp.setErrorMessage("��¼ʧ��:" + resp.getErrorMessage());
				return resp;
			}
			SingleSmRequestBean singleSms = new SingleSmRequestBean();
			singleSms.setSessionID(resp.getSeqId());
			singleSms.setDestAddr(sDstPhone);
			singleSms.setOrdAddr("10657301110");
			singleSms.setContext(sMsgText);
			singleSms.setFeeCode("0");
			singleSms.setFeeType("02");

			resp = sms_stub.sendSignleSM(singleSms);
			if (resp.getErrorCode() != 0) {
				resp.setErrorMessage("���ŷ���ʧ��:" + resp.getErrorMessage());
			}
		} catch (Exception e) {
			resp.setErrorCode(-1);
			resp.setErrorMessage("ϵͳ����:" + e.getMessage());
		}

		return resp;
	}*/
}
