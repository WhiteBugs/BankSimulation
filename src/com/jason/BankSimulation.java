package com.jason;

import java.util.Random;

public class BankSimulation {
	private  Queue q1 = new Queue(),
			       q2 = new Queue(),
			       q3 = new Queue(),
			       q4 = new Queue();
	private  EventList eventList;
	private  int afterTimeTorrent = 9;
	private  int handleTimeTorrent = 30;
	
	
    public static void main(String[] args) {
		new BankSimulation().runSimulation(1000);;
	}
	
	public BankSimulation(){
		eventList = new EventList(new Event(0,EventType.newCustomArrive));
		q1.setLeaveType(EventType.firstQueueLeave);
		q2.setLeaveType(EventType.secondQueueLeave);
		q3.setLeaveType(EventType.thirdQueueLeave);
		q4.setLeaveType(EventType.fourthQueueLeave);	
	}

	void runSimulation(int continuedTime){
		int currentTime = 0;
		int passedTime=0;
		int peopleArriveNum=0;
		int peopleHavedFinish=0;
		Random random = new Random();
		Event currentEvent = null;
		
		while(true){
		    currentEvent = eventList.getFirstEvent();	
			currentTime = currentEvent.getOccurTime();
			Customer currentCustomer = null;
			int dealTime = 0;
			switch (currentEvent.getEventType()) {
			case newCustomArrive:
				peopleArriveNum++;
				Customer newCustomer = new Customer(currentEvent.getOccurTime(),random.nextInt(handleTimeTorrent));
				newCustomer.setNumber(peopleArriveNum);
			 	int afterTime = random.nextInt(afterTimeTorrent);
			 	eventList.insertEvent(new Event(afterTime+currentEvent.getOccurTime(), EventType.newCustomArrive));
			 	Queue shortestQueue = shortestQueue();
				shortestQueue.insert(newCustomer);
				if(shortestQueue.getSize()==1){
					eventList.insertEvent(new Event(currentTime+shortestQueue.getFirstNode().getCustomer().getHandleTime(),shortestQueue.getLeaveType()));
				}
				System.out.println("第"+currentTime+"分钟：第"+newCustomer.getNumber()+"顾客到达银行");
				break;

			case firstQueueLeave:
				peopleHavedFinish++;
				dealTime = currentTime - q1.getFirstNode().getCustomer().getArriveTime();
				passedTime = passedTime + dealTime;
				currentCustomer = q1.getFirstNode().getCustomer();
				System.out.print("第"+currentTime+"分钟：第一队列顾客离开银行，总耗时"+dealTime+"分钟    排队时间为"+(dealTime-currentCustomer.getHandleTime())+"分钟    处理时间为："+currentCustomer.getHandleTime()+"分钟，为第"+currentCustomer.getNumber()+"顾客       ");
				System.out.println("已完成业务的顾客数："+peopleHavedFinish);
				q1.deleteFirstNode();
				if(q1.getSize()>1){
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
				if(q2.getSize()>1){
					eventList.insertEvent(new Event(currentTime+q2.getFirstNode().getCustomer().getHandleTime(), EventType.secondQueueLeave));
				}
				break;
				
			case thirdQueueLeave:
				peopleHavedFinish++;
				dealTime = currentTime - q3.getFirstNode().getCustomer().getArriveTime();
				passedTime = passedTime + dealTime;
				currentCustomer = q3.getFirstNode().getCustomer();
				System.out.print("第"+currentTime+"分钟：第二队列顾客离开银行，总耗时"+dealTime+"分钟    排队时间为"+(dealTime-currentCustomer.getHandleTime())+"分钟    处理时间为："+currentCustomer.getHandleTime()+"分钟，为第"+currentCustomer.getNumber()+"顾客    ");
				System.out.println("已完成业务的顾客数："+peopleHavedFinish);
				q3.deleteFirstNode();
				if(q3.getSize()>1){
					eventList.insertEvent(new Event(currentTime+q3.getFirstNode().getCustomer().getHandleTime(), EventType.thirdQueueLeave));
				}
				break;
				
			case fourthQueueLeave:
				peopleHavedFinish++;
				dealTime = currentTime - q4.getFirstNode().getCustomer().getArriveTime();
				passedTime = passedTime + dealTime;
				currentCustomer = q4.getFirstNode().getCustomer();
				System.out.print("第"+currentTime+"分钟：第二队列顾客离开银行，总耗时"+dealTime+"分钟    排队时间为"+(dealTime-currentCustomer.getHandleTime())+"分钟    处理时间为："+currentCustomer.getHandleTime()+"分钟 ，为第"+currentCustomer.getNumber()+"顾客     ");
				System.out.println("已完成业务的顾客数："+peopleHavedFinish);
				q4.deleteFirstNode();
				if(q4.getSize()>1){
					eventList.insertEvent(new Event(currentTime+q4.getFirstNode().getCustomer().getHandleTime(), EventType.fourthQueueLeave));
				}
				break;
		
			default:
				break;
			}
			eventList.removeFirstEvent();
			if(currentTime>continuedTime){
				break;
			}
		}
		double avergeTime = passedTime/peopleHavedFinish;
		System.out.println("完成业务的顾客数："+peopleHavedFinish);
		System.out.println("平均处理时间为"+avergeTime);
	}
	
	 Queue shortestQueue(){
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

class Queue{
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

class EventList{
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
	public void insertEvent(Event event){
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


class Event{
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
class Customer{
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

class QueueNode{
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
class EventNode{
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

enum EventType{
	newCustomArrive,
	firstQueueLeave,
	secondQueueLeave,
	thirdQueueLeave,
	fourthQueueLeave
}
