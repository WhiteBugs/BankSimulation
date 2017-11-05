package com.jason;

import java.util.Random;

import javax.swing.JOptionPane;

public class BankSimulation {
	private  Queue q1 = new Queue(),
			       q2 = new Queue(),
			       q3 = new Queue(),
			       q4 = new Queue();
	private  EventList eventList;
	private  int afterTimeTorrent = 0;//下一个顾客到达时间的最大值限定
	private  int handleTimeTorrent = 0;//顾客处理时间的最大值限定
	
	
    public static void main(String[] args) {
    	/*String temp = JOptionPane.showInputDialog("两个相邻到达银行的客户的时间间隔不超过    （分钟）");
    	int afterTimeTorrent = Integer.parseInt(temp);
    	temp = JOptionPane.showInputDialog("每个客户办理业务的时间不超过    （分钟）");
    	int handleTimeTorrent = Integer.parseInt(temp);
    	temp = JOptionPane.showInputDialog("银行开门时间为  （分钟）");
    	int continuedTime = Integer.parseInt(temp);*/
    	BankSimulation bankSimulation = new BankSimulation( 5, 20 );
		bankSimulation.runSimulation(40);//参数为银行营业时间，单位为分钟
	}
	
	public BankSimulation(int afterTimeTorrent , int handleTimeTorrent){
		this.afterTimeTorrent = afterTimeTorrent;
		this.handleTimeTorrent = handleTimeTorrent;
		eventList = new EventList(new Event(0,EventType.newCustomArrive));//初始化，插入一个到达事件
		/*初始化四条队列*/
		q1.setLeaveType(EventType.firstQueueLeave);
		q2.setLeaveType(EventType.secondQueueLeave);
		q3.setLeaveType(EventType.thirdQueueLeave);
		q4.setLeaveType(EventType.fourthQueueLeave);	
	}

	void runSimulation(int continuedTime){
		int currentTime = 0;//当前时间
		int passedTime=0;//当前事件距离开门已经历的时间
		int peopleArriveNum=0;//到达银行的顾客数
		int peopleHavedFinish=0;//处理完事件离开银行的顾客数
		Random random = new Random();
		Event currentEvent = null;//当前事务链表的第一个结点的事件
		
		while(true){
		    currentEvent = eventList.getFirstEvent();
			currentTime = currentEvent.getOccurTime();
			Customer currentCustomer = null;
			int dealTime = 0;//当前顾客总耗时，包括排队时间和处理时间
			switch (currentEvent.getEventType()) {//对事件类型进行判断
			case newCustomArrive:
				peopleArriveNum++;
				Customer newCustomer = new Customer(currentEvent.getOccurTime(),random.nextInt(handleTimeTorrent));//为当前时间新建一个顾客对象
				newCustomer.setNumber(peopleArriveNum);
			 	int afterTime = random.nextInt(afterTimeTorrent);//产生下一个顾客到达时间间隔
			 	eventList.insertEvent(new Event(afterTime+currentEvent.getOccurTime(), EventType.newCustomArrive));//插入新的顾客到达事件
			 	Queue shortestQueue = shortestQueue();//获取最短的一条队伍
				shortestQueue.insert(newCustomer);//将顾客插入该队伍队尾
				int queueNum = 0;
				switch (shortestQueue.getLeaveType()) {//判断最短的是哪条队伍
				case firstQueueLeave:
					queueNum = 1;
					break;
				case secondQueueLeave:
					queueNum = 2;
					break;
				case thirdQueueLeave:
					queueNum = 3;
					break;
				case fourthQueueLeave:
					queueNum = 4;
					break;
				default:
					break;
				}
				if(shortestQueue.getSize()==1){//若队伍插入前为空队伍，则插入顾客后同时产生一顾客离开事件
					eventList.insertEvent(new Event(currentTime+shortestQueue.getFirstNode().getCustomer().getHandleTime(),shortestQueue.getLeaveType()));
				}
				System.out.println("第"+currentTime+"分钟：第"+newCustomer.getNumber()+"个顾客到达银行       进入第"+queueNum+"队列");
				break;

			case firstQueueLeave:
				peopleHavedFinish++;
				dealTime = currentTime - q1.getFirstNode().getCustomer().getArriveTime();//当前顾客耗费的时间
				passedTime = passedTime + dealTime;//银行开门到现在的时间
				currentCustomer = q1.getFirstNode().getCustomer();
				System.out.print("第"+currentTime+"分钟：第一队列顾客离开银行，总耗时"+dealTime+"分钟    排队时间为"+(dealTime-currentCustomer.getHandleTime())+"分钟    处理时间为："+currentCustomer.getHandleTime()+"分钟，为第"+currentCustomer.getNumber()+"顾客       ");
				System.out.println("已完成业务的顾客数："+peopleHavedFinish);
				q1.deleteFirstNode();
				if(q1.getSize()>0){//顾客离开后若队伍不为空，则为队首顾客产生一离开事件
					eventList.insertEvent(new Event(currentTime+q1.getFirstNode().getCustomer().getHandleTime(), EventType.firstQueueLeave));
				}
				break;
				
			case secondQueueLeave:
				peopleHavedFinish++;
				dealTime = currentTime - q2.getFirstNode().getCustomer().getArriveTime();
				passedTime = passedTime + dealTime;
				currentCustomer = q2.getFirstNode().getCustomer();
				System.out.print("第"+currentTime+"分钟：第二队列顾客离开银行，总耗时"+dealTime+"分钟    排队时间为"+(dealTime-currentCustomer.getHandleTime())+"分钟    处理时间为："+currentCustomer.getHandleTime()+"分钟，为第"+currentCustomer.getNumber()+"顾客     ");
				System.out.println("已完成业务的顾客数："+peopleHavedFinish);
				q2.deleteFirstNode();
				if(q2.getSize()>0){
					eventList.insertEvent(new Event(currentTime+q2.getFirstNode().getCustomer().getHandleTime(), EventType.secondQueueLeave));
				}
				break;
				
			case thirdQueueLeave:
				peopleHavedFinish++;
				dealTime = currentTime - q3.getFirstNode().getCustomer().getArriveTime();
				passedTime = passedTime + dealTime;
				currentCustomer = q3.getFirstNode().getCustomer();
				System.out.print("第"+currentTime+"分钟：第三队列顾客离开银行，总耗时"+dealTime+"分钟    排队时间为"+(dealTime-currentCustomer.getHandleTime())+"分钟    处理时间为："+currentCustomer.getHandleTime()+"分钟，为第"+currentCustomer.getNumber()+"顾客    ");
				System.out.println("已完成业务的顾客数："+peopleHavedFinish);
				q3.deleteFirstNode();
				if(q3.getSize()>0){
					eventList.insertEvent(new Event(currentTime+q3.getFirstNode().getCustomer().getHandleTime(), EventType.thirdQueueLeave));
				}
				break;
				
			case fourthQueueLeave:
				peopleHavedFinish++;
				dealTime = currentTime - q4.getFirstNode().getCustomer().getArriveTime();
				passedTime = passedTime + dealTime;
				currentCustomer = q4.getFirstNode().getCustomer();
				System.out.print("第"+currentTime+"分钟：第四队列顾客离开银行，总耗时"+dealTime+"分钟    排队时间为"+(dealTime-currentCustomer.getHandleTime())+"分钟    处理时间为："+currentCustomer.getHandleTime()+"分钟 ，为第"+currentCustomer.getNumber()+"顾客     ");
				System.out.println("已完成业务的顾客数："+peopleHavedFinish);
				q4.deleteFirstNode();
				if(q4.getSize()>0){
					eventList.insertEvent(new Event(currentTime+q4.getFirstNode().getCustomer().getHandleTime(), EventType.fourthQueueLeave));
				}
				break;
		
			default:
				break;
			}
			eventList.removeFirstEvent();//删除事务链表第一个结点
			if(currentTime>continuedTime){//判断是否达到关门时间，若为真，则跳出循环，结束仿真
				break;
			}
		}
		double avergeTime = passedTime/peopleHavedFinish;
		System.out.println("完成业务的顾客数："+peopleHavedFinish);
		System.out.println("平均处理时间为"+avergeTime);
		
	}
	
	 Queue shortestQueue(){//返回四条队伍中最短的队伍
		Queue shortestQueue = q1;
		if(shortestQueue.getSize()>q2.getSize()){
			shortestQueue = q2;
		}
		if(shortestQueue.getSize()>q3.getSize()){
			shortestQueue = q3;
		}
		if(shortestQueue.getSize()>q4.getSize()){
			shortestQueue = q4;
		}
		return shortestQueue;
	}
}

class Queue{//队列类
	private QueueNode firstNode;
	private QueueNode lastNode;
	private int size=0;
	private EventType leaveType;
	
	public void insert(Customer customer){
		
		if(firstNode==null){
			firstNode = new QueueNode(customer);
			lastNode = firstNode;
		}else{
			lastNode.setNext(new QueueNode(customer));
			lastNode = lastNode.getNext();
		}
		size++;
	}
	public void setLeaveType(EventType eventType){
		this.leaveType = eventType;
	}
	public EventType getLeaveType(){
		return leaveType;
	}
	public QueueNode getFirstNode(){
		return this.firstNode;
	}
	public int getSize(){
		return size;
	}
	public void deleteFirstNode(){
		firstNode = firstNode.getNext();
		size--;
	}
}
class EventList{//事务链表类
	private EventNode firstNode;
	
	public EventList(Event fisrtEvent){
		firstNode = new EventNode(fisrtEvent);
	}
	
	public Event getFirstEvent(){
		return firstNode.getEvent();
	}
	public void removeFirstEvent(){
		firstNode = firstNode.getNext();
	}
	public void insertEvent(Event event){//按时间先后顺序插入事件
		EventNode currentNode = firstNode;
		EventNode nextNode = firstNode.getNext();
		
		while(true){
			if(nextNode==null){
				currentNode.setNext(new EventNode(event));
				nextNode = currentNode.getNext();
				break;
			}else if(event.getOccurTime()<=nextNode.getEvent().getOccurTime()){
				currentNode.setNext(new EventNode(event));
				currentNode.getNext().setNext(nextNode);
				break;
			}else if(event.getOccurTime()>nextNode.getEvent().getOccurTime()){
				currentNode = nextNode;
				nextNode = nextNode.getNext();
			}
		}
	}
}
class Event{//事件类，包含发生时间和事件类型
	private int occurTime;
	private EventType eventType;
	
	public Event(int occurTime,EventType eventType){
		this.occurTime = occurTime;
		this.eventType = eventType;
	}
	
	public void setOccurTime(int occurTime){
		this.occurTime = occurTime;
	}
	public int getOccurTime(){
		return occurTime;
	}
	public void setEventType(EventType eventType){
		this.eventType = eventType;
	}
	public EventType getEventType(){
		return eventType;
	}
}
class Customer{//顾客类
	private int arriveTime;
	private int handleTime;
	private int number;
	
	public Customer(int arriveTime,int handleTime){
		this.arriveTime = arriveTime;
		this.handleTime = handleTime;
	}
	
	public void setNumber(int n){
		this.number = n;
	}
	public int getNumber(){
		return number;
	}
	public void setArriveTime(int arriveTime){
		this.arriveTime=arriveTime;
	}
	public int getArriveTime(){
		return arriveTime;
	}
	public void setHandleTime(int handleTime){
		this.handleTime=handleTime;
	}
	public int getHandleTime(){
		return handleTime;
	}
}
class QueueNode{//队伍结点，包含指向下一个结点的引用和一个顾客对象
	private Customer customer;
	private QueueNode next;
	
	public QueueNode(Customer customer){
		this.customer = customer;
	}
	public QueueNode getNext(){
		return next;
	}
	public void setNext(QueueNode next){
		this.next = next;
	}
	public Customer getCustomer(){
		return customer;
	}
	public void  setCustomer(Customer customer){
		this.customer = customer;
	}
}
class EventNode{//事务链表的结点，包含指向下一个结点的引用和一个事件对象
	private Event event;
	private EventNode next=null;
	
	public EventNode(Event event){
		this.event=event;
	}
	public EventNode getNext(){
		return next;
	}
	public void setNext(EventNode next){
		this.next = next;
	}
	public Event getEvent(){
		return event;
	}
	public void setEvent(Event event){
		this.event = event;
	}
}

enum EventType{//枚举类，包含五个事件类型
	newCustomArrive,
	firstQueueLeave,
	secondQueueLeave,
	thirdQueueLeave,
	fourthQueueLeave
}
