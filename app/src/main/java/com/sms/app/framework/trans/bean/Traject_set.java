package com.sms.app.framework.trans.bean;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/4/17.
 */

public class Traject_set {

    private Long user_id;
    private Long total_num;
    private Long start_index;
    private Long end_index;
    private List<Trajectory> trajectories = new ArrayList<Trajectory>();
    
    
    public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getTotal_num() {
		return total_num;
	}

	public void setTotal_num(Long total_num) {
		this.total_num = total_num;
	}

	public Long getStart_index() {
		return start_index;
	}

	public void setStart_index(Long start_index) {
		this.start_index = start_index;
	}

	public Long getEnd_index() {
		return end_index;
	}

	public void setEnd_index(Long end_index) {
		this.end_index = end_index;
	}

	public List<Trajectory> getTrajectories() {
		return trajectories;
	}



	public void setTrajectories(List<Trajectory> trajectories) {
		this.trajectories = trajectories;
	}


	public static void main(String[] ar)
    {
    	Traject_set set = new Traject_set();
    	Trajectory tras[] = new Trajectory[10];
    	Point ps[] =  new Point[5];
    	for(int i = 0 ; i < ps.length;i++)ps[i]=new Point();
    	for(int i = 0 ; i < tras.length ; i++)
    	{
    		tras[i] = new Trajectory();
    		for(int j = 0 ; j<ps.length ; j++)tras[i].getPoints().add(ps[j]);
    		set.getTrajectories().add(tras[i]);
    	}
    	
    	Gson json =  new Gson();
    	System.out.println(json.toJson(set));
    	
    }

	@Override
	public String toString() {
		return "Traject_set{" +
				"user_id=" + user_id +
				", total_num=" + total_num +
				", start_index=" + start_index +
				", end_index=" + end_index +
				", trajectories=" + trajectories +
				'}';
	}
}
