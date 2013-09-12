package ru.sabstest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;


abstract public class Packet{

	public enum Type{PacketEPD, PacketEPDVER, PacketESIDVER_RYM, PacketEPDVER_B, PacketESID, 
		ED201, ED208, ED243, ED244, ED273, ED708_VER, ED743_VER, ED744_VER, ED773_VER};

		public String filename;

		public Sign firstSign;
		public Sign secondSign;

		public boolean isVER = true;

		//реквизиты ЭД
		public int edNo; //Номер ЭД в течение опердня
		public Date edDate; //Дата составления ЭД
		public String edAuthor; //Уникальный идентификатор составителя ЭД (УИС)
		public String edReceiver; //УИС составителя


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
			+ ((edAuthor == null) ? 0 : edAuthor.hashCode());
			result = prime * result + ((edDate == null) ? 0 : edDate.hashCode());
			result = prime * result
			+ ((edReceiver == null) ? 0 : edReceiver.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Packet other = (Packet) obj;
			if (edAuthor == null) {
				if (other.edAuthor != null)
					return false;
			} else if (!edAuthor.equals(other.edAuthor))
				return false;
			if (edDate == null) {
				if (other.edDate != null)
					return false;
			} else if (!edDate.equals(other.edDate))
				return false;
			if (edReceiver == null) {
				if (other.edReceiver != null)
					return false;
			} else if (!edReceiver.equals(other.edReceiver))
				return false;
			return true;
		}

		public int compareTo(Packet o) {		
			return filename.compareTo(o.filename);		
		}	

		public void insertForRead()
		{
			if(!isVER)
				DB.insertPacetForReadUfebs(filename);
			else
				DB.insertPacetForReadVer(filename);
		}

		public Element getEncodedElement(String src, boolean isUTF)
		{
			Element root;
			if(isUTF)
				root = XML.getXMLRootElementFromStringUTF(XML.decodeBase64(src));
			else
				root = XML.getXMLRootElementFromString1251(XML.decodeBase64(src));

			return root;
		}



		public static ReadED createReadEDByFile(String type)
		{
			ReadED pc = null;	

			Type t = Type.valueOf(type);

			switch(t)
			{
			case PacketEPD:
				pc = new PacketEPD();
				break;
			case PacketEPDVER:
				pc = new PacketEPD();
				break;
			case PacketESIDVER_RYM:
				pc = new PacketESIDVER();
				break;
			case PacketEPDVER_B:
				pc = new PacketEPDVER_B();
				break;
			case PacketESID:
				pc = new PacketESID();
				break;
			case ED208:
				pc = new ED208();
				break;
			case ED243:
				pc = new ED243();
				break;
			case ED244:
				pc = new ED244();
				break;
			case ED273:
				pc = new ED273();
				break;
			case ED708_VER:
				pc = new ED708_VER();
				break;
			case ED743_VER:
				pc = new ED743_VER();
				break;
			case ED744_VER:
				pc = new ED744_VER();
				break;
			case ED773_VER:
				pc = new ED773_VER();
				break;
			}

			return pc;
		}

		public static Generate<Element> createGenFromXMLByFile(String type)
		{
			Generate<Element> pc = null;		

			Type t = Type.valueOf(type);

			switch(t)
			{
			case PacketEPD:
				pc = new PacketEPD();
				break;
			case PacketEPDVER:
				pc = new PacketEPD();
				break;
			case ED243:
				pc = new ED243();
				break;
			case ED743_VER:
				pc = new ED743_VER();
				break;
			case ED273:
				pc = new ED273();
				break;
			}

			return pc;
		}

		public static List<Generate<PacketEPD>> createGenFromEPD()
		{
			List<Generate<PacketEPD>> pc = new ArrayList<Generate<PacketEPD>>(); 
			pc.add(new PacketESIDVER());
			pc.add(new PacketEPDVER_B());			
			return pc;
		}


		public static List<Generate<ED743_VER>> createGenFromED743(int size)
		{
			List<Generate<ED743_VER>> pc = new ArrayList<Generate<ED743_VER>>();
			for(int i = 0; i < size; i++)
			{
				pc.add(new ED708_VER());
				pc.add(new ED744_VER());
			}
			return pc;
		}

		public static List<Generate<ED243>> createGenFromED243(int size)
		{
			List<Generate<ED243>> pc = new ArrayList<Generate<ED243>>();
			for(int i = 0; i < size; i++)
			{
				pc.add(new ED244());			
			}
			return pc;
		}

		public static List<Generate<ED773_VER>> createGenFromED773(ReadEDList source)
		{
			List<Generate<ED773_VER>> pc = new ArrayList<Generate<ED773_VER>>();
			for(ReadED re:source.pList)
			{
				for(int i = 0; i < ((ED773_VER) re).ed.pdList.size(); i++)
				{
					pc.add((new ED708_VER(i)).getGenerateED773());
					pc.add(new ED774_VER(i));
				}
			}
			return pc;
		}

		public Sign[] getSigns()
		{
			Sign[] sgn = {firstSign, secondSign};
			return sgn;
		}

		public boolean isVER()
		{			
			return isVER;
		}

		public void readXML(Element root) {
			edNo = Integer.parseInt(root.getAttribute("EDNo"));
			edDate = Date.valueOf(root.getAttribute("EDDate"));
			edAuthor = root.getAttribute("EDAuthor");
			edReceiver = root.getAttribute("EDReceiver");
		}

		public int insertIntoDBPacket(DB db, int sum, String type)
		{
			int idPacket = 0;
			String uic = Settings.bik.substring(2) + "000";
			if(isVER)
			{
				try{


					String query =  "INSERT INTO [dbo].[epay_Packet]\r\n" + 
					"([ID_Depart], [ID_ARM], [User_Insert], [InOutMode],\r\n" + 
					" [Date_Oper], [EDNo], [EDDate], [EDAuthor], [EDReceiver], [EDQuantity],\r\n" + 
					" [Summa], [SystemCode], [sTime], [Type], [FileName], [KodErr], [KodObr],\r\n" + 
					" [KodDocum], [Time_Inp], [MSGID], [ErrorTxt], [Mesto], [MesFrom], [MesType],\r\n" + 
					" [MesPrior], [MesFName], [MesTime], [SlKonv], [Pridenti], [Shifr], [Upakovka],\r\n" + 
					" [OID], [WriteSig], [Verify], [Pr_ufebs], [Forme221], [IEdNo], [IEdDate], \r\n" + 
					"[IEdAuth], [Esc_Key], [Esc_key2], [Seanc], [FilePath], [ManName], [QueName], [KcoiKgur], [TypeObr])\r\n" + 
					"VALUES(null, 0, null, 0,\r\n" + 
					DB.toString(Settings.operDate) + ", " + DB.toString(edNo) + ", " + DB.toString(edDate) + ", " + DB.toString(edAuthor) + ", " + DB.toString(edReceiver) + ", 1,\r\n" + 
					DB.toString(sum) + ", null, null, " + DB.toString(type) + ", " + DB.toString(filename) + ", 0, 0, \r\n" + 
					" 0, null, null, null, " + DB.toString(uic) + ", " + DB.toString(uic) + ", 1, \r\n" +  //Mesto MesFrom?
					" 5, null, '20120202', 3, 1, 0, 0,\r\n" + 
					" 0, 4, 3, 0, 1, null, null,\r\n" + 
					" null, null, null, 20, '', NULL, NULL, NULL, '4')";			
					db.st.executeUpdate(query);


					idPacket = Integer.parseInt(DB.selectFirstValueSabsDb("select max(ID_PACKET) from dbo.epay_Packet"));
				} catch (Exception e) {
					e.printStackTrace();
					Log.msg(e);			
				}
			}
			else
			{
				try{
					String query = "INSERT INTO [dbo].[UFEBS_Pacet]\r\n" + 
					"( [ID_Depart], [ID_ARM], [User_Insert], [InOutMode], \r\n" + 
					"[Date_Oper], [pacno], [pacdate], [Author], [Receiver], [Quantity],\r\n" + 
					" [SumPac], [SystCode], [sTime], [Type], [FileName], [KodErr], [KodObr],\r\n" + 
					" [KodDocum], [Time_Inp], [MSGID], [ErrorTxt], [Mesto], [MesFrom], [MesType],\r\n" + 
					" [MesPrior], [MesFName], [MesTime], [SlKonv], [Pridenti], [Shifr], [Upakovka],\r\n" + 
					" [OID], [WriteSig], [Verify], [Pr_ufebs], [Forme221], [IEdNo], [IEdDate],\r\n" + 
					" [IEdAuth], [Esc_Key], [Esc_key2], [Seanc], [FilePath], [ManName], [QueName], [KcoiKgur], [TypeObr])\r\n" + 
					"VALUES(null, 0, null, 0,\r\n" + 
					DB.toString(Settings.operDate) + ", " + DB.toString(edNo) + ", " + DB.toString(edDate) + ", " + DB.toString(edAuthor) + ", " + DB.toString(edReceiver) + ", 1,\r\n" + 
					DB.toString(sum) + ", null, null, " + DB.toString(type) + ", " + DB.toString(filename) + ", 0, 0, \r\n" + 
					" 0, null, null, null, " + DB.toString(uic) + ", " + DB.toString(edAuthor) + ", 1, \r\n" +  //Mesto MesFrom?
					" 5, null, '20120202', 3, 1, 1, 1,\r\n" + 
					" 0, 1, 3, 0, 1, null, null,\r\n" + 
					" null, null, null, 20, '', NULL, NULL, NULL, '4')";			
					db.st.executeUpdate(query);


					idPacket = Integer.parseInt(DB.selectFirstValueSabsDb("select max(ID_PACET) from dbo.UFEBS_Pacet"));
				} catch (Exception e) {
					e.printStackTrace();
					Log.msg(e);			
				}
			}
			return idPacket;
		}
}
