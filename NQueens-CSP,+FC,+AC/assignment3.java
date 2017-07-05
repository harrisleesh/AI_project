/*
 * 
 * Class name : 인공 지능
 * 
 * 이 프로그램의 목적 : 이 프로그램은 N-Queens 문제를 standard CSP, forward checking, arc consistency 방법으로 풀이하는 프로그램입니다.
 * 
 * 주안점 : constraints를 각 열에서 퀸이 위치 할 수 있는 cell로 정했습니다.
 * 
 * 시간의 단위는 sec 입니다.
 * 
 * 출력은 각 0 ~ N-1 까지의 Column에 있는 각 Queen의 row의 위치를 출력합니다.
 * 
 * 문제를 풀 수 없는경우 No solution을 출력하고 시간은 0.0을 출력합니다.
 * 
 * 최대한 code conventions에 맞춰서 프로그래밍 하고자 노력했습니다.
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
	static double elapsed_time_CSP = 0;		// csp 경과 시간

	
	public static boolean ind = false;
	public static void main(String[] args){
		
		
		Num = Integer.parseInt( args[0]);						// 퀸의 갯수
		ArrayList <Integer> goal = new ArrayList<Integer>();	// N_Queen ArrayList
		
		
		//constraints generation
		List cstrs = new ArrayList();
		cstrs = cstGenerate(Num);
		
		
		// 파일 이름 형식
		String filename = new String();
		filename = "result";
		filename += Num;
		filename += ".txt";
		
		
		// 파일 출력 class를 이용
		File output = new File( args[1] , filename );
		PrintWriter outputStream = null;
		try {
			outputStream = new PrintWriter(new FileOutputStream(output));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// 출력 형식 맞추기
		outputStream.println( ">Standard CSP" );
		goal = Csp(cstrs);
		if( Num >= 4){
			for( int i = 0 ; i < Num ; i++ )									// goal에 있는 퀸의 열당 행의 위치를 출력
				outputStream.print( goal.get( i ) + " " );				// 행의 넘버를 1~N으로 계산 해서 출력 형식을 맞추기 위해 -1을 함.
			outputStream.println();
			outputStream.println( "Total Elapsed Time: " + elapsed_time_CSP );	// 경과 시간 출력
		}
		else {																// 해답을 찾을 수 없을 경우
			outputStream.println( "No Solution" );
			outputStream.println( "Total Elapsed Time: 0.000" );
		}
		
		outputStream.println( ">CSP with Forward Checking" );
		goal = Csp_with_forward(cstrs);
		if( Num >= 4){
			for( int i = 0 ; i < Num ; i++ )									// goal에 있는 퀸의 열당 행의 위치를 출력
				outputStream.print( goal.get( i ) + " " );				// 행의 넘버를 1~N으로 계산 해서 출력 형식을 맞추기 위해 -1을 함.
			outputStream.println();
			outputStream.println( "Total Elapsed Time: " + elapsed_time_CSP );	// 경과 시간 출력
		}
		else {																// 해답을 찾을 수 없을 경우
			outputStream.println( "No Solution" );
			outputStream.println( "Total Elapsed Time: 0.000" );
		}
		
		outputStream.println( ">CSP with Arc Consistency" );
		goal = Csp_with_arc(cstrs);
		if( Num >= 4){
			for( int i = 0 ; i < Num ; i++ )									// goal에 있는 퀸의 열당 행의 위치를 출력
				outputStream.print( goal.get( i ) + " " );				// 행의 넘버를 1~N으로 계산 해서 출력 형식을 맞추기 위해 -1을 함.
			outputStream.println();
			outputStream.println( "Total Elapsed Time: " + elapsed_time_CSP );	// 경과 시간 출력
		}
		else {																// 해답을 찾을 수 없을 경우
			outputStream.println( "No Solution" );
			outputStream.println( "Total Elapsed Time: 0.000" );
		}
		outputStream.close();												// outputStream 종료
	
	
		
		
		return;
	}
	
	
	
	//포워드 체킹 함수
	public static List forward_check(List cstrs, int index){
		
		ArrayList<Integer> set = new ArrayList<Integer>();
		set.addAll((ArrayList<Integer>)cstrs.get(index)); 		//index의 constraint의 모든 값을 set에 저장
		for( int i = index+1 ; i < Num; i++){
			ArrayList<Integer> temp = new ArrayList<Integer>();
			int location = 0;
			temp.addAll((ArrayList<Integer>)cstrs.get(i));			//i번째 constraint의 값을 temp에 저장
			location = set.get(0);								//index의 queen의 위치를 location의 저장
			
			if(temp.indexOf(location)!=-1)						//constraint의 값이 i번째 queen과 같은 행이라면 지운다.
				temp.remove(temp.indexOf(location));
			
			
			// 대각선에 있는 constraint를 지워준다.
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
	
	//arc consistency 체크 함수
	public static List arc_consistency_check(List cstrs, int index){
		for( int i = index ; i < Num-1; i++){
			ArrayList<Integer> set = new ArrayList<Integer>();
			set.addAll((ArrayList<Integer>)cstrs.get(index)); 		//index의 constraint의 모든 값을 set에 저장
			List arc = new ArrayList();
			arc.addAll(cstrs);
			
			
			
			// arc consistency 에서는 forward checking을 모든 열에서 반복해 준다.
			for(int j=0; j<set.size();j++){
				int location = set.get(j);
				
				for(int k=i+1; k < Num ; k++){
					ArrayList<Integer> temp = new ArrayList<Integer>();
					
					temp.addAll((ArrayList<Integer>)cstrs.get(k));			//i번째 constraint의 값을 temp에 저장
					
					if(temp.indexOf(location)!=-1)						//constraint의 값이 i번째 queen과 같은 행이라면 지운다.
						temp.remove(temp.indexOf(location));
					
					
					// 대각선에 있는 constraint를 지워준다.
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
	
	// 해당 인덱스의 constraint를 확인하여 배치할 수 있는 퀸의 열만을 남기고 나머지는 삭제하는 함수
	public static List satisCstr(List cstrs, int index){
		ArrayList<Integer> temp = new ArrayList<Integer>();
		ArrayList<Integer> set = new ArrayList<Integer>();
		set.addAll((ArrayList<Integer>)cstrs.get(index)); 		//index의 constraint의 모든 값을 set에 저장
		for( int i = 0 ; i < index; i++){
			int location = 0;
			temp = (ArrayList<Integer>)cstrs.get(i);			//i번째 constraint의 값을 temp에 저장
			location = temp.get(0);								//queen의 위치를 location의 저장
			if(set.indexOf(location)!=-1)						//constraint의 값이 i번째 queen과 같은 행이라면 지운다.
				set.remove(set.indexOf(location));
			
			
			// 대각선에 있는 constraint를 지워준다.
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
	
	
	// 제약 조건을 모두 만족된 ArrayList에서 goal의 ArrayList를 만든다.
	public static ArrayList makegoal(List node){
		ArrayList <Integer> goal = new ArrayList();
		ArrayList <Integer> temp = new ArrayList();
		for(int i=0 ; i< Num; i++){
			temp = (ArrayList<Integer>)node.get(i);
			goal.add(temp.get(0));
		}
		
		return goal;
	}
	
	
	//standard CSP 함수
	public static ArrayList Csp(List cstrs){
		elapsed_time_CSP = System.currentTimeMillis(); 		// Search의 시작 시간을 저장

		
		//DFS를 위한 stack 선언
		Stack <List> dfs = new Stack<List>();
		Stack <Integer> dfs_num = new Stack<Integer>();
		ArrayList <Integer> first_node = new ArrayList <Integer>();
		
		first_node.addAll((ArrayList<Integer>)cstrs.get(0));
		
		
		//스택에 초기 상태를 추가
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
		
		
		//스택이 empty일 떄 정지
		while(!dfs.isEmpty()){
			ArrayList <Integer> search_index = new ArrayList <Integer>();
			ArrayList <Integer> current_constraint = new ArrayList <Integer>();
			List temp_constraint = new ArrayList(); 
			temp_constraint.addAll(dfs.pop());
			int index = dfs_num.pop();
			
			
			boolean empty = false;
			
			// 제약조건의 arraylist가 비어있을 때 정지
			for( int i = 0; i<Num;i++){
				search_index = (ArrayList<Integer>)temp_constraint.get(i);
				if(search_index.size()==0){
					empty = true;
				}
			}
			
			if(empty == false){
				temp_constraint = satisCstr(temp_constraint,index);
				current_constraint.addAll((ArrayList<Integer>)temp_constraint.get(index));
				
				//현재 index의 +1 한 값이 Queen의 숫자와 같다면 goal을 return
				if(index+1==Num && current_constraint.size()==1){
					elapsed_time_CSP = ( System.currentTimeMillis() - elapsed_time_CSP ) / 1000 ;
					return makegoal(temp_constraint);
					
					
				}
				
				
			
				//해당 index의 constraint가 적어도 1은 되어야 진행
				if(current_constraint.size()!=0){
					for (int i = 0 ; i < current_constraint.size(); i++){
						ArrayList <Integer> selected_constraint = new ArrayList <Integer>();
						temp_constraint.remove(index);
						selected_constraint.add(current_constraint.get(i));
						temp_constraint.add(index, selected_constraint);
						List new_constraint = new ArrayList();
						new_constraint.addAll(temp_constraint);		
						dfs.add(new_constraint);					//dfs에 새로운 제약 조건 추가
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
	
	//CSP with 포워드 체킹 함수
	public static ArrayList Csp_with_forward(List cstrs){
		elapsed_time_CSP = System.currentTimeMillis(); 		// Search의 시작 시간을 저장

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
		
		
		//dfs가 비어있을 때 정지
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
				
				//현재 index의 +1 한 값이 Queen의 숫자와 같다면 goal을 return
				if(index+1==Num && current_constraint.size()==1){
					
					elapsed_time_CSP = ( System.currentTimeMillis() - elapsed_time_CSP ) / 1000 ;
					return makegoal(temp_constraint);
				}
				
			
				//해당 index의 constraint가 적어도 1은 되어야 진행
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
	
	
	// CSP with arc consistency함수
	public static ArrayList Csp_with_arc(List cstrs){
		elapsed_time_CSP = System.currentTimeMillis(); 		// Search의 시작 시간을 저장
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
		
		
		// stack이 비어있을 경우 멈춘다.
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
				
				//현재 index의 +1 한 값이 Queen의 숫자와 같다면 goal을 return
				if(index+1==Num && current_constraint.size()==1){
					elapsed_time_CSP = ( System.currentTimeMillis() - elapsed_time_CSP ) / 1000 ;
					return makegoal(temp_constraint);
				}
				
				
			
				//해당 index의 constraint가 적어도 1은 되어야 진행
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
	
	// 초기 제약조건 리스트를 생성
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
