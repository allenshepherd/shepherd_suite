package data_parser;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;

public class reader {
	
	
	//c:\Users\akshephe\Downloads\CTL (1).csv

    String csvFile = "c:\\Users\\akshephe\\Downloads\\test.csv";
    String line = "";
    String cvsSplitBy = ",";
    double purchase_funds=200000;
    double purchase_price=0;
    double sell_price=0;
    int i =0;
    String[][] arrayitems;
    
	public void test2()
	{
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			while ((line = br.readLine()) != null) {
			i=i+1;
			}
		} catch (IOException e) {
	    }

		arrayitems = new String[i][7];
		i=0;
	    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
	        while ((line = br.readLine()) != null) {
	            String[] inputCsvData = line.split(",");
	           // System.out.println(" Purchase Price($$) =" + inputCsvData[1] + "@"+ inputCsvData[0] +" ]	::  " +
	            //	" Sell Price($$) = " + inputCsvData[1] + "@"+ inputCsvData[0] + "] : 	DURATION: 8 days" );
	            if (i>0){
	            	arrayitems[i][0]=inputCsvData[0];
	            	arrayitems[i][1]=inputCsvData[1];
	            	arrayitems[i][2]=inputCsvData[2];
	            	arrayitems[i][3]=inputCsvData[3];
	            	arrayitems[i][4]=inputCsvData[4];
	            	arrayitems[i][5]=inputCsvData[5];
	            	arrayitems[i][6]=inputCsvData[6];
	            }
	            i=i+1;
	        }
	    } catch (IOException e) {
	    	 System.out.println("testing error");
	        e.printStackTrace();
	    }
	    purchase_stock();
	    //purchase_strategy_2();
	}
	
	
	public void purchase_stock(){
		double current_potential_price;
		double previous_potential_price=0;
		double sell_price=0;
		int shares=0;
		boolean buyflag=true;
		double growth_trigger=1.005;
		i=253;
		while (i>0){
			//System.out.println(arrayitems[i][0]+"   "+ buyflag);
			if (buyflag==true){
				current_potential_price=(1/growth_trigger)*Double.parseDouble(arrayitems[i][1]);
				if(previous_potential_price>=Double.parseDouble(arrayitems[i][3]) ) {
					//buy shares
					shares=(int) (purchase_funds/previous_potential_price);
					purchase_funds-=shares*previous_potential_price;
					//System.out.println("funds "+ purchase_funds +" share amount "+ shares);
					System.out.println("Buy shares on "+arrayitems[i][0]+" at this price " +previous_potential_price +" this is account value :"+purchase_funds);
					sell_price=previous_potential_price*growth_trigger;
					previous_potential_price=0;
					current_potential_price=0;
					buyflag=false;
					
					if(Double.parseDouble(arrayitems[i][4]) >= sell_price ) {
						//sell shares
						System.out.println("SAME DAY shares on "+arrayitems[i][0]+" at this price " +sell_price + " this is account value :"+purchase_funds);
						purchase_funds+=shares*sell_price;
						//System.out.println("SELLfunds "+ purchase_funds +" share amount "+ shares);
						buyflag=true;
						//i+=1;
					}
				}
				else if(current_potential_price>=Double.parseDouble(arrayitems[i][3]) )
				{
					shares=(int) (purchase_funds/current_potential_price);
					purchase_funds-=shares*current_potential_price;
					//System.out.println("funds "+ purchase_funds +" share amount "+ shares);
					System.out.println("BUY SHARES on "+arrayitems[i][0]+" at this price " +current_potential_price +" this is account value :"+purchase_funds);
					sell_price=current_potential_price*growth_trigger;
					previous_potential_price=0;
					current_potential_price=0;
					buyflag=false;
					if(Double.parseDouble(arrayitems[i][4]) >= sell_price ) {
						//sell shares
						System.out.println("   Sell shares on "+arrayitems[i][0]+" at this price " +sell_price + " this is account value :"+purchase_funds);
						purchase_funds+=shares*sell_price;
						//System.out.println("SELLfunds "+ purchase_funds +" share amount "+ shares);
						buyflag=true;
						//i+=1;
					}
				}
				else {
					previous_potential_price=current_potential_price;	
				}
			} 
			else{ //buy flag is false... need to sell....
				if(Double.parseDouble(arrayitems[i][2]) >= sell_price ) {
					//sell shares
					System.out.println("   Sell shares on "+arrayitems[i][0]+" at this price " +sell_price + " this is account value :"+purchase_funds);
					purchase_funds+=shares*sell_price;
					//System.out.println("SELLfunds "+ purchase_funds +" share amount "+ shares);
					buyflag=true;
					//i+=1;
				}	
			}
			
			i-=1;
		}
		purchase_funds+=shares*(sell_price*1/growth_trigger);
		shares=0;
		System.out.println("final funds "+ purchase_funds +" share amount "+ shares +" final sell_price "+ sell_price);
	}

	//this plan will buy incremental shares, and sell incremental shares as the price average out
	public void purchase_strategy_2(){
		double current_potential_price;
		double previous_potential_price=0;
		double sell_price=0;
		double average_purchase_price=0;
		int share_multiplier=10;
		int shares=0;
		double factor=1;
		//boolean buyflag=true;
		double growth_trigger=1.015;
		double growth=0;
		i=253;
		while (i>0){

			if((Double.parseDouble(arrayitems[i][2]) >= growth_trigger*average_purchase_price ) && (shares > 0)) {
				//sell shares
				purchase_funds+=shares*growth_trigger*average_purchase_price;
				System.out.println("   SSell shares ("+shares+") on "+arrayitems[i][0]+" at this price " +sell_price + " this is account value :"+purchase_funds);
				shares=0;
				factor=1;
				//System.out.println("SELLfunds "+ purchase_funds +" share amount "+ shares);
			}
				current_potential_price=(1/growth_trigger)*Double.parseDouble(arrayitems[i][1]);
				if(previous_potential_price>=Double.parseDouble(arrayitems[i][3]) ) {
					//buy shares
					if (purchase_funds >= share_multiplier*previous_potential_price){
					purchase_funds-=share_multiplier*previous_potential_price*factor;
					factor+=growth;
					//System.out.println("funds "+ purchase_funds +" share amount "+ shares);
					shares+=share_multiplier;
					if (shares==share_multiplier)
						average_purchase_price=(share_multiplier*previous_potential_price)/shares;	
					else
						average_purchase_price= ((average_purchase_price* (shares-share_multiplier))+(share_multiplier*previous_potential_price))/shares;	
					
					System.out.println("Buy shares ("+shares+") on "+arrayitems[i][0]+" at this price " +average_purchase_price +" this is account value :"+purchase_funds);
					previous_potential_price=0;
					current_potential_price=0;
					}
					else{
						//System.out.println("cant purchase - out of funds at this price "+previous_potential_price);
						previous_potential_price=current_potential_price;
					}
					
				}
				else if(current_potential_price>=Double.parseDouble(arrayitems[i][3]) )
				{
					if (purchase_funds >= share_multiplier*current_potential_price){
					purchase_funds-=share_multiplier*current_potential_price*factor;
					factor+=growth;
					//System.out.println("funds "+ purchase_funds +" share amount "+ shares);
					shares+=share_multiplier;
					//average_purchase_price=(share_multiplier*current_potential_price)/shares;
					if (shares==share_multiplier)
						average_purchase_price=(share_multiplier*previous_potential_price)/shares;	
					else
						average_purchase_price= ((average_purchase_price* (shares-share_multiplier))+(share_multiplier*current_potential_price))/shares;	
					
					System.out.println("BUY SHARES ("+shares+") on "+arrayitems[i][0]+" at this price " +average_purchase_price +" this is account value :"+purchase_funds);
					previous_potential_price=0;
					current_potential_price=0;
					}
					else{
						//System.out.println("can't purchase with "+purchase_funds+" - out of funds " +(share_multiplier*current_potential_price));
						previous_potential_price=current_potential_price;
					}
				}
				else {
					previous_potential_price=current_potential_price;	
				}
				if((Double.parseDouble(arrayitems[i][4]) >= growth_trigger*average_purchase_price ) && (shares > 0)) {
					//sell shares
					purchase_funds+=shares*growth_trigger*average_purchase_price;
					System.out.println("   Sell shares ("+shares+") on "+arrayitems[i][0]+" at this price " +(growth_trigger*average_purchase_price) + " this is account value :"+purchase_funds);
					shares=0;
					factor=1;
					//System.out.println("SELLfunds "+ purchase_funds +" share amount "+ shares);
				}	
			i-=1;
		}
		//purchase_funds+=shares*(sell_price*1/growth_trigger);
		//shares=0;
		System.out.println("final funds "+ purchase_funds +" share amount "+ shares +" final stock value "+ (purchase_funds + shares*Double.parseDouble(arrayitems[1][4])) );
	}

}


