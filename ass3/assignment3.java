/*
 * 
 * Class name : �ΰ� ����
 * 
 * �� ���α׷��� ���� : �� ���α׷��� N-Queens ������ standard CSP, forward checking, arc consistency ������� Ǯ���ϴ� ���α׷��Դϴ�.
 * 
 * �־��� : constraints�� �� ������ ���� ��ġ �� �� �ִ� cell�� ���߽��ϴ�.
 * 
 * �ð��� ������ sec �Դϴ�.
 * 
 * ����� �� 0 ~ N-1 ������ Column�� �ִ� �� Queen�� row�� ��ġ�� ����մϴ�.
 * 
 * ������ Ǯ �� ���°�� No solution�� ����ϰ� �ð��� 0.0�� ����մϴ�.
 * 
 * �ִ��� code conventions�� ���缭 ���α׷��� �ϰ��� ����߽��ϴ�.
 * 
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class assignment3 {
	public static int Num;
	static double elapsed_time_CSP = 0;		// csp ��� �ð�

	
	public static boolean ind = false;
	public static void main(String[] args){
		
		
		Num = Integer.parseInt( args[0]);						// ���� ����
		ArrayList <Integer> goal = new ArrayList<Integer>();	// N_Queen ArrayList
		
		
		//constraints generation
		List cstrs = new ArrayList();
		cstrs = cstGenerate(Num);
		
		
		// ���� �̸� ����
		String filename = new String();
		filename = "result";
		filename += Num;
		filename += ".txt";
		
		
		// ���� ��� class�� �̿�
		File output = new File( args[1] , filename );
		PrintWriter outputStream = null;
		try {
			outputStream = new PrintWriter(new FileOutputStream(output));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// ��� ���� ���߱�
		outputStream.println( ">Standard CSP" );
		goal = Csp(cstrs);
		if( Num >= 4){
			for( int i = 0 ; i < Num ; i++ )									// goal�� �ִ� ���� ���� ���� ��ġ�� ���
				outputStream.print( goal.get( i ) + " " );				// ���� �ѹ��� 1~N���� ��� �ؼ� ��� ������ ���߱� ���� -1�� ��.
			outputStream.println();
			outputStream.println( "Total Elapsed Time: " + elapsed_time_CSP );	// ��� �ð� ���
		}
		else {																// �ش��� ã�� �� ���� ���
			outputStream.println( "No Solution" );
			outputStream.println( "Total Elapsed Time: 0.000" );
		}
		
		outputStream.println( ">CSP with Forward Checking" );
		goal = Csp_with_forward(cstrs);
		if( Num >= 4){
			for( int i = 0 ; i < Num ; i++ )									// goal�� �ִ� ���� ���� ���� ��ġ�� ���
				outputStream.print( goal.get( i ) + " " );				// ���� �ѹ��� 1~N���� ��� �ؼ� ��� ������ ���߱� ���� -1�� ��.
			outputStream.println();
			outputStream.println( "Total Elapsed Time: " + elapsed_time_CSP );	// ��� �ð� ���
		}
		else {																// �ش��� ã�� �� ���� ���
			outputStream.println( "No Solution" );
			outputStream.println( "Total Elapsed Time: 0.000" );
		}
		
		outputStream.println( ">CSP with Arc Consistency" );
		goal = Csp_with_arc(cstrs);
		if( Num >= 4){
			for( int i = 0 ; i < Num ; i++ )									// goal�� �ִ� ���� ���� ���� ��ġ�� ���
				outputStream.print( goal.get( i ) + " " );				// ���� �ѹ��� 1~N���� ��� �ؼ� ��� ������ ���߱� ���� -1�� ��.
			outputStream.println();
			outputStream.println( "Total Elapsed Time: " + elapsed_time_CSP );	// ��� �ð� ���
		}
		else {																// �ش��� ã�� �� ���� ���
			outputStream.println( "No Solution" );
			outputStream.println( "Total Elapsed Time: 0.000" );
		}
		outputStream.close();												// outputStream ����
	
	
		
		
		return;
	}
	
	
	
	//������ üŷ �Լ�
	public static List forward_check(List cstrs, int index){
		
		ArrayList<Integer> set = new ArrayList<Integer>();
		set.addAll((ArrayList<Integer>)cstrs.get(index)); 		//index�� constraint�� ��� ���� set�� ����
		for( int i = index+1 ; i < Num; i++){
			ArrayList<Integer> temp = new ArrayList<Integer>();
			int location = 0;
			temp.addAll((ArrayList<Integer>)cstrs.get(i));			//i��° constraint�� ���� temp�� ����
			location = set.get(0);								//index�� queen�� ��ġ�� location�� ����
			
			if(temp.indexOf(location)!=-1)						//constraint�� ���� i��° queen�� ���� ���̶�� �����.
				temp.remove(temp.indexOf(location));
			
			
			// �밢���� �ִ� constraint�� �����ش�.
			if(location+(i-index) < 8 ){
				if(temp.indexOf(location+(i-index))!=-1)
					temp.remove(temp.indexOf(location+(i-index)));
			}
			if(location-(i-index) >=0 ){
				if(temp.indexOf(location-(i-index))!=-1)
					temp.remove(temp.indexOf(location-(i-index)));
			}
			cstrs.remove(i);
			cstrs.add(i, temp);
			
			
		}
		
		
		return cstrs;
	}
	
	//arc consistency üũ �Լ�
	public static List arc_consistency_check(List cstrs, int index){
		for( int i = index ; i < Num-1; i++){
			ArrayList<Integer> set = new ArrayList<Integer>();
			set.addAll((ArrayList<Integer>)cstrs.get(index)); 		//index�� constraint�� ��� ���� set�� ����
			List arc = new ArrayList();
			arc.addAll(cstrs);
			
			
			
			// arc consistency ������ forward checking�� ��� ������ �ݺ��� �ش�.
			for(int j=0; j<set.size();j++){
				int location = set.get(j);
				
				for(int k=i+1; k < Num ; k++){
					ArrayList<Integer> temp = new ArrayList<Integer>();
					
					temp.addAll((ArrayList<Integer>)cstrs.get(k));			//i��° constraint�� ���� temp�� ����
					
					if(temp.indexOf(location)!=-1)						//constraint�� ���� i��° queen�� ���� ���̶�� �����.
						temp.remove(temp.indexOf(location));
					
					
					// �밢���� �ִ� constraint�� �����ش�.
					if(location+(i-k) < 8 ){
						if(temp.indexOf(location+(i-k))!=-1)
							temp.remove(temp.indexOf(location+(i-k)));
					}
					if(location-(i-k) >=0 ){
						if(temp.indexOf(location-(i-k))!=-1)
							temp.remove(temp.indexOf(location-(i-k)));
					}
					arc.remove(k);
					arc.add(k, temp);
				}
				
			}
		}
		
		
		return cstrs;
	}
	
	// �ش� �ε����� constraint�� Ȯ���Ͽ� ��ġ�� �� �ִ� ���� ������ ����� �������� �����ϴ� �Լ�
	public static List satisCstr(List cstrs, int index){
		ArrayList<Integer> temp = new ArrayList<Integer>();
		ArrayList<Integer> set = new ArrayList<Integer>();
		set.addAll((ArrayList<Integer>)cstrs.get(index)); 		//index�� constraint�� ��� ���� set�� ����
		for( int i = 0 ; i < index; i++){
			int location = 0;
			temp = (ArrayList<Integer>)cstrs.get(i);			//i��° constraint�� ���� temp�� ����
			location = temp.get(0);								//queen�� ��ġ�� location�� ����
			if(set.indexOf(location)!=-1)						//constraint�� ���� i��° queen�� ���� ���̶�� �����.
				set.remove(set.indexOf(location));
			
			
			// �밢���� �ִ� constraint�� �����ش�.
			if(location+(index-i) < 8 ){
				if(set.indexOf(location+(index-i))!=-1)
					set.remove(set.indexOf(location+(index-i)));
			}
			if(location-(index-i) >=0 ){
				if(set.indexOf(location-(index-i))!=-1)
					set.remove(set.indexOf(location-(index-i)));
			}
			
		}
		
		cstrs.remove(index);
		cstrs.add(index, set);
		
		return cstrs;
	}
	
	
	// ���� ������ ��� ������ ArrayList���� goal�� ArrayList�� �����.
	public static ArrayList makegoal(List node){
		ArrayList <Integer> goal = new ArrayList();
		ArrayList <Integer> temp = new ArrayList();
		for(int i=0 ; i< Num; i++){
			temp = (ArrayList<Integer>)node.get(i);
			goal.add(temp.get(0));
		}
		
		return goal;
	}
	
	
	//standard CSP �Լ�
	public static ArrayList Csp(List cstrs){
		elapsed_time_CSP = System.currentTimeMillis(); 		// Search�� ���� �ð��� ����

		
		//DFS�� ���� stack ����
		Stack <List> dfs = new Stack<List>();
		Stack <Integer> dfs_num = new Stack<Integer>();
		ArrayList <Integer> first_node = new ArrayList <Integer>();
		
		first_node.addAll((ArrayList<Integer>)cstrs.get(0));
		
		
		//���ÿ� �ʱ� ���¸� �߰�
		for (int i = 0 ; i < first_node.size(); i++){
			List constraint_arr1 = new ArrayList();
			ArrayList <Integer> first_constraint = new ArrayList <Integer>();
			constraint_arr1.addAll(cstrs);
			constraint_arr1.remove(0);
			first_constraint.add(first_node.get(i));
			constraint_arr1.add(0,first_constraint);
			dfs.add(constraint_arr1);
			dfs_num.add(0);
		}
		
		
		//������ empty�� �� ����
		while(!dfs.isEmpty()){
			ArrayList <Integer> search_index = new ArrayList <Integer>();
			ArrayList <Integer> current_constraint = new ArrayList <Integer>();
			List temp_constraint = new ArrayList(); 
			temp_constraint.addAll(dfs.pop());
			int index = dfs_num.pop();
			
			
			boolean empty = false;
			
			// ���������� arraylist�� ������� �� ����
			for( int i = 0; i<Num;i++){
				search_index = (ArrayList<Integer>)temp_constraint.get(i);
				if(search_index.size()==0){
					empty = true;
				}
			}
			
			if(empty == false){
				temp_constraint = satisCstr(temp_constraint,index);
				current_constraint.addAll((ArrayList<Integer>)temp_constraint.get(index));
				
				//���� index�� +1 �� ���� Queen�� ���ڿ� ���ٸ� goal�� return
				if(index+1==Num && current_constraint.size()==1){
					elapsed_time_CSP = ( System.currentTimeMillis() - elapsed_time_CSP ) / 1000 ;
					return makegoal(temp_constraint);
					
					
				}
				
				
			
				//�ش� index�� constraint�� ��� 1�� �Ǿ�� ����
				if(current_constraint.size()!=0){
					for (int i = 0 ; i < current_constraint.size(); i++){
						ArrayList <Integer> selected_constraint = new ArrayList <Integer>();
						temp_constraint.remove(index);
						selected_constraint.add(current_constraint.get(i));
						temp_constraint.add(index, selected_constraint);
						List new_constraint = new ArrayList();
						new_constraint.addAll(temp_constraint);		
						dfs.add(new_constraint);					//dfs�� ���ο� ���� ���� �߰�
						if (ind == false){
							dfs_num.add(index);
							ind = true;
						}
						else
							dfs_num.add(index+1);
							
					}
				}
			}
		}
		return first_node;
	}
	
	//CSP with ������ üŷ �Լ�
	public static ArrayList Csp_with_forward(List cstrs){
		elapsed_time_CSP = System.currentTimeMillis(); 		// Search�� ���� �ð��� ����

		Stack <List> dfs = new Stack<List>();
		Stack <Integer> dfs_num = new Stack<Integer>();
		ArrayList <Integer> first_node = new ArrayList <Integer>();
		
		first_node.addAll((ArrayList<Integer>)cstrs.get(0));
		
		for (int i = 0 ; i < first_node.size(); i++){
			List constraint_arr1 = new ArrayList();
			ArrayList <Integer> first_constraint = new ArrayList <Integer>();
			constraint_arr1.addAll(cstrs);
			constraint_arr1.remove(0);
			first_constraint.add(first_node.get(i));
			constraint_arr1.add(0,first_constraint);
			dfs.add(constraint_arr1);
			dfs_num.add(0);
		}
		
		
		//dfs�� ������� �� ����
		while(!dfs.isEmpty()){
			ArrayList <Integer> search_index = new ArrayList <Integer>();
			ArrayList <Integer> current_constraint = new ArrayList <Integer>();
			//ArrayList <Integer> temp_5 = new ArrayList <Integer>();
			List temp_constraint = new ArrayList(); 
			temp_constraint.addAll(dfs.pop());
			int index = dfs_num.pop();
			
			
			
			
			boolean empty = false;
			//find if it is empty or not
			for( int i = 0; i<Num;i++){
				search_index = (ArrayList<Integer>)temp_constraint.get(i);
				if(search_index.size()==0){
					empty = true;
				}
			}
			
			if(empty == false){
				temp_constraint = satisCstr(temp_constraint,index);
				current_constraint.addAll((ArrayList<Integer>)temp_constraint.get(index));
				
				//���� index�� +1 �� ���� Queen�� ���ڿ� ���ٸ� goal�� return
				if(index+1==Num && current_constraint.size()==1){
					
					elapsed_time_CSP = ( System.currentTimeMillis() - elapsed_time_CSP ) / 1000 ;
					return makegoal(temp_constraint);
				}
				
			
				//�ش� index�� constraint�� ��� 1�� �Ǿ�� ����
				if(current_constraint.size()!=0){
					for (int i = 0 ; i < current_constraint.size(); i++){
						ArrayList <Integer> selected_constraint = new ArrayList <Integer>();
						List temp2_constraint = new ArrayList ();
						temp2_constraint.addAll(temp_constraint);
						temp2_constraint.remove(index);
						selected_constraint.add(current_constraint.get(i));
						temp2_constraint.add(index, selected_constraint);
						List new_constraint = new ArrayList();
						temp2_constraint = forward_check(temp2_constraint,index);		//forward check
						new_constraint.addAll(temp2_constraint);
						dfs.add(new_constraint);
						dfs_num.add(index+1);
							
					}
				}
			}
		}
		return first_node;
	}
	
	
	// CSP with arc consistency�Լ�
	public static ArrayList Csp_with_arc(List cstrs){
		elapsed_time_CSP = System.currentTimeMillis(); 		// Search�� ���� �ð��� ����
		Stack <List> dfs = new Stack<List>();
		Stack <Integer> dfs_num = new Stack<Integer>();
		ArrayList <Integer> first_node = new ArrayList <Integer>();
		
		first_node.addAll((ArrayList<Integer>)cstrs.get(0));
		
		for (int i = 0 ; i < first_node.size(); i++){
			List constraint_arr1 = new ArrayList();
			ArrayList <Integer> first_constraint = new ArrayList <Integer>();
			constraint_arr1.addAll(cstrs);
			constraint_arr1.remove(0);
			first_constraint.add(first_node.get(i));
			constraint_arr1.add(0,first_constraint);
			dfs.add(constraint_arr1);
			dfs_num.add(0);
		}
		
		
		// stack�� ������� ��� �����.
		while(!dfs.isEmpty()){
			ArrayList <Integer> search_index = new ArrayList <Integer>();
			ArrayList <Integer> current_constraint = new ArrayList <Integer>();
			List temp_constraint = new ArrayList(); 
			temp_constraint.addAll(dfs.pop());
			int index = dfs_num.pop();
			
			
			
			
			boolean empty = false;
			//find if it is empty or not
			for( int i = 0; i<Num;i++){
				search_index = (ArrayList<Integer>)temp_constraint.get(i);
				if(search_index.size()==0){
					empty = true;
				}
			}
			
			if(empty == false){
				temp_constraint = satisCstr(temp_constraint,index);
				current_constraint.addAll((ArrayList<Integer>)temp_constraint.get(index));
				
				//���� index�� +1 �� ���� Queen�� ���ڿ� ���ٸ� goal�� return
				if(index+1==Num && current_constraint.size()==1){
					elapsed_time_CSP = ( System.currentTimeMillis() - elapsed_time_CSP ) / 1000 ;
					return makegoal(temp_constraint);
				}
				
				
			
				//�ش� index�� constraint�� ��� 1�� �Ǿ�� ����
				if(current_constraint.size()!=0){
					for (int i = 0 ; i < current_constraint.size(); i++){
						ArrayList <Integer> selected_constraint = new ArrayList <Integer>();
						List temp2_constraint = new ArrayList ();
						temp2_constraint.addAll(temp_constraint);
						temp2_constraint.remove(index);
						selected_constraint.add(current_constraint.get(i));
						temp2_constraint.add(index, selected_constraint);	
						List new_constraint = new ArrayList();
						temp2_constraint = arc_consistency_check(temp2_constraint,index);	//arc consistency check
						new_constraint.addAll(temp2_constraint);
						dfs.add(new_constraint);
						dfs_num.add(index+1);
							
					}
				}
			}
		}
		return first_node;
	}
	
	// �ʱ� �������� ����Ʈ�� ����
	public static List cstGenerate(int N){
		ArrayList <Integer> constraints =  new ArrayList<Integer>();
		List cstrs = new ArrayList();
		for (int i=0;i<N;i++){
			constraints.add(i);
		}
		for (int j=0;j<N;j++){
			cstrs.add(constraints);	
		}
		
		return cstrs; 
	}
}
