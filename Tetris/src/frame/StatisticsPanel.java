package frame;
//***************************************************************************************
//StatisticPanel.java                                   Author:  Beibei Zhang
//
//to show the player and computer statistic information in a TextArea.
//invoked by the correspond menu in the mainpanel/
//
//in this panel, prefix p means player, prefix c means computer.
//***************************************************************************************

import java.awt.*;
import javax.swing.*;

public class StatisticsPanel extends JPanel
{
    private static final long serialVersionUID = 1L;
	private JTextArea statisticsTextArea;
	private int hour, minute, second;
	private int pline, cline, papm, capm, pbpm, cbpm; 
	private float pactionperbrick, cactionperbrick, pbrickpersend, cbrickpersend, psendperminute, csendperminute, psendperdispose, csendperdispose;
	private final Font RULE_FONT = new Font("Italic",Font.PLAIN,20);
	
	//	***************************************************************************************
	//  pstatistics and cstatistics are arrays from playserpanel and computerpanel
	//	element 0 is total action
	//	element 1 is total brick dropped
	//  element 2 is one-row-dispose times
	//	element 3 is two-row-dispose times
	//	element 4 is three-row-dispose times
	//	element 5 is four-row-dispose times
	//	element 6 is total rows sent to oppenent
	//	
	//  seconds is gamerunning time, get from mainpanel timetimer.
	//	***************************************************************************************
	public StatisticsPanel (int[] pstatistics, int[] cstatistics, int seconds) {
		
		statisticsTextArea = new JTextArea(30,30);
		statisticsTextArea.setFont(RULE_FONT);

		// calculate all kinds of statistic number.		
		hour = (int) seconds/3600;
		minute = (int) (seconds-hour*3600)/60;
		second = seconds-hour*3600-minute*60;
		pline = pstatistics[2]+pstatistics[3]*2+pstatistics[4]*3+pstatistics[5]*4;
		cline = cstatistics[2]+cstatistics[3]*2+cstatistics[4]*3+cstatistics[5]*4;
		papm = 60*pstatistics[0]/seconds;
		capm = 60*cstatistics[0]/seconds;
		pbpm = 60*pstatistics[1]/seconds;
		cbpm = 60*cstatistics[1]/seconds;
		pactionperbrick = ((float) pstatistics[0])/pstatistics[1];
		cactionperbrick = ((float) cstatistics[0])/cstatistics[1];
		pbrickpersend = ((float) pstatistics[1])/pstatistics[6];
		cbrickpersend = ((float) cstatistics[1])/cstatistics[6];
		psendperdispose = ((float) pstatistics[6])/pline;
		csendperdispose = ((float) cstatistics[6])/cline;
		psendperminute = 60*((float) pstatistics[6])/seconds;
		csendperminute = 60*((float) cstatistics[6])/seconds;
		
		statisticsTextArea.append("Gametime: ");
		if (hour > 0)   statisticsTextArea.append(hour+"h");
		statisticsTextArea.append(minute+"m"+second+"s"+"\n");
		statisticsTextArea.append("---------------------"+"\n"+"\n");
		
		statisticsTextArea.append("player:"+"\n");
		statisticsTextArea.append("---------------------"+"\n");
		statisticsTextArea.append("Total action: "+pstatistics[0]+"\n");
		statisticsTextArea.append("Total brick: "+pstatistics[1]+"\n");
		statisticsTextArea.append("Total row disposed: "+pline+"\n");
		statisticsTextArea.append("   one-row-dispose times: "+pstatistics[2]+"\n");
		statisticsTextArea.append("   two-row-dispose times: "+pstatistics[3]+"\n");
		statisticsTextArea.append("   three-row-dispose times: "+pstatistics[4]+"\n");
		statisticsTextArea.append("   four-row-dispose times: "+pstatistics[5]+"\n");
		statisticsTextArea.append("Total row sent: "+pstatistics[6]+"\n");
		statisticsTextArea.append("Action per minute: "+papm+"\n");
		statisticsTextArea.append("Brick per minute: "+pbpm+"\n");
		statisticsTextArea.append("Action per brick: "+pactionperbrick+"\n");
		statisticsTextArea.append("Brick per send: "+pbrickpersend+"\n");
		statisticsTextArea.append("Send per dispose: "+psendperdispose+"\n");
		statisticsTextArea.append("Send per minuter: "+psendperminute+"\n");
		statisticsTextArea.append("\n");
		
		statisticsTextArea.append("computer:"+"\n");
		statisticsTextArea.append("---------------------"+"\n");
		statisticsTextArea.append("Total action: "+cstatistics[0]+"\n");
		statisticsTextArea.append("Total brick: "+cstatistics[1]+"\n");
		statisticsTextArea.append("Total row disposed: "+cline+"\n");
		statisticsTextArea.append("   one-row-dispose times: "+cstatistics[2]+"\n");
		statisticsTextArea.append("   two-row-dispose times: "+cstatistics[3]+"\n");
		statisticsTextArea.append("   three-row-dispose times: "+cstatistics[4]+"\n");
		statisticsTextArea.append("   four-row-dispose times: "+cstatistics[5]+"\n");
		statisticsTextArea.append("Total row sent: "+cstatistics[6]+"\n");
		statisticsTextArea.append("Action per minute: "+capm+"\n");
		statisticsTextArea.append("Brick per minute: "+cbpm+"\n");
		statisticsTextArea.append("Action per brick: "+cactionperbrick+"\n");
		statisticsTextArea.append("Brick per send: "+cbrickpersend+"\n");
		statisticsTextArea.append("Send per dispose: "+csendperdispose+"\n");
		statisticsTextArea.append("Send per minuter: "+csendperminute+"\n");
		
		add(statisticsTextArea);
	}
}