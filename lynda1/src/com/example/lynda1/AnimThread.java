package com.example.lynda1;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;

public class AnimThread extends Thread{
	
	private SurfaceHolder holder;
	private boolean running;
	//private int i=0;
	//private int j=0;
	Canvas canvas = null;
	
	Paint paint = new Paint();
	Rect myrect = new Rect();
	
	long mStartTime = System.currentTimeMillis();
	
	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////
	
	float canwidth;// deprecated
	float canheight;  // deprecated
	int thick=10;
	int over=0;
	int binary=1;
	int clicked=0;
	int n_nx;
	int n_ny;
	
	int[] stackx;
	int[] stacky;
	int[] stackm;
	
	int ttop=-1;
	int curi=2;
	int curj=2;
	int cantgo=0,up=0,down=0,right=0,left=0;
	int move=1;
	int px = 2, py = 2;
	ArrayList<Node> answer = new ArrayList<Node>();
	//Node[] answer;
	Node[][] nodes;
	int ran;
	int repf;
	boolean theflag_is = true;
	boolean hasstarted = true;
	
	boolean watchMazeGen = false;
	
	///////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
	
	
	public AnimThread(SurfaceHolder holder) {
		this.holder = holder;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub	
		if(theflag_is)
		{ 
			canvas = holder.lockCanvas();
			theflag_is = false;
			
			canwidth = canvas.getWidth();// deprecated
			canheight = canvas.getHeight();  // deprecated
			n_nx=(int)(canwidth/thick);
			n_ny=(int)(canheight/thick);
			
			stackx=new int[n_ny*n_nx];
			stacky=new int[n_ny*n_nx];
			stackm=new int[n_ny*n_nx];
			
			nodes=new Node[n_ny][n_nx];
			
		//canvas.getWidth();
		//canvas.getHeight();
		
		for(int i=0;i<n_ny;i++)
			for(int j=0;j<n_nx;j++)
				nodes[i][j]=new Node(j*thick,i*thick,0);
					
			for(int i=0;i<n_ny;i++)
				{		
				nodes[i][0].v=1;
				nodes[i][n_nx-1].v=1;
				}	

			for(int i=0;i<n_nx;i++)
				{
				nodes[0][i].v=1;
				nodes[n_ny-1][i].v=1;
				}	
				
			nodes[curi][curj].v=1;
			ttop+=1;
			stackx[ttop]=nodes[curi][curj].x;
			stacky[ttop]=nodes[curi][curj].y;
			stackm[ttop]=1;
			
			paint.setColor(Color.RED);
			for(int i=0;i<n_ny;i++)
				for(int j=0;j<n_nx;j++)
					if(nodes[i][j].v==1)
					{
			//int i=0, j=0;
					
					myrect.set(nodes[i][j].x,nodes[i][j].y, nodes[i][j].x+thick, nodes[i][j].y+thick);
					canvas.drawRect(myrect, paint);
					
					}
			if(watchMazeGen)
				holder.unlockCanvasAndPost(canvas);
				}
		
		
		
		while(running ) {
			
            try {
            	//canvas = holder.lockCanvas();
              
            	synchronized (holder) {
            		doit();
                }	
            } //catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			//}
            finally {
                    //if (canvas != null)
                            //holder.unlockCanvasAndPost(canvas);
                    }               
            }
		if(!watchMazeGen)
			holder.unlockCanvasAndPost(canvas);
        
		while(true) {
        	play();
        	try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		
	}
	
	void play()
	{
	if(Math.abs(RotationValues.x)>Math.abs(RotationValues.y))
	{
	if(RotationValues.x<0 && px<(n_nx-1) && nodes[py][px+1].v!=0) px++;
	else if(RotationValues.x>0 && px>2 && nodes[py][px-1].v!=0) px--;
	}
	else
	{
	if(RotationValues.y>0 && py<(n_ny-1) && nodes[py+1][px].v!=0) py++;
	else if(RotationValues.y<0 && py>2 && nodes[py-1][px].v!=0) py--;
	}
	//var px=Math.floor(e.clientX/thick);
	//var py=Math.floor(e.clientY/thick);
	
	//if(clicked==1)
	if(nodes[py][px].v==1)
	{
	if((nodes[py-1][px].x==answer.get(answer.size()-1).x && nodes[py-1][px].y==answer.get(answer.size()-1).y) || (nodes[py+1][px].x==answer.get(answer.size()-1).x && nodes[py+1][px].y==answer.get(answer.size()-1).y) || (nodes[py][px-1].x==answer.get(answer.size()-1).x && nodes[py][px-1].y==answer.get(answer.size()-1).y) || (nodes[py][px+1].x==answer.get(answer.size()-1).x && nodes[py][px+1].y==answer.get(answer.size()-1).y))
		{
		nodes[py][px].v=2;
		//nodes[py][px].c="blue";
		answer.add(nodes[py][px]);
		
		paint.setColor(Color.BLUE);
		myrect.set(nodes[py][px].x,nodes[py][px].y, nodes[py][px].x+thick, nodes[py][px].y+thick);
		canvas = holder.lockCanvas(myrect);
		canvas.drawRect(myrect, paint);
		holder.unlockCanvasAndPost(canvas);
		
//		ctx.beginPath();	
//		ctx.rect(nodes[py][px].x,nodes[py][px].y,thick,thick);
//		ctx.fillStyle="blue";
//		ctx.fill();
//		ctx.closePath();
		}
	}
	else if(answer.size()>1)
	{
		if(answer.get(answer.size()-2).x==(px*thick) && answer.get(answer.size()-2).y==(py*thick))
		{
		px=(int)(answer.get(answer.size()-1).x/thick);
		py=(int)(answer.get(answer.size()-1).y/thick);
		answer.remove(answer.size()-1);
		nodes[py][px].v=1;
		//nodes[py][px].c="white";
		
		paint.setColor(Color.WHITE);
		myrect.set(nodes[py][px].x,nodes[py][px].y, nodes[py][px].x+thick, nodes[py][px].y+thick);
		canvas = holder.lockCanvas(myrect);
		canvas.drawRect(myrect, paint);
		holder.unlockCanvasAndPost(canvas);
		
//		ctx.beginPath();	
//		ctx.rect(nodes[py][px].x,nodes[py][px].y,thick,thick);
//		ctx.fillStyle="white";
//		ctx.fill();
//		ctx.closePath();
		}
	}
	
	}

	
	void setRunning(boolean b) {
		// TODO Auto-generated method stub
		running = b;
	}

	public void setxy(float x2, float y2) {
		// TODO Auto-generated method stub
		//x = x2;
		//y = y2;
	}
		
	private void doit() {
		
		//clearTimeout(repf);
		/////////////////////////////////////
		/////////////////////////////////////
				
		/////////////////////////////////////
		/////////////////////////////////////
		
		//if(wait_then_draw) {
		
		int[] list=new int[]{1,2,3,4};
		up=down=right=left=cantgo=0;
		
		switch(move)
			{
			case 3: 
				list[0]=4;
				list[1]=2;
				list[2]=3;
				list[3]=1;
				//list=[4,2,3,1];
				if(nodes[curi][curj-2].v==binary || nodes[curi][curj-1].v==binary || nodes[curi-1][curj-1].v==binary || nodes[curi+1][curj-1].v==binary || nodes[curi-1][curj-2].v==binary || nodes[curi+1][curj-2].v==binary)
					{left=1;cantgo++;}
				if(nodes[curi][curj+2].v==binary || nodes[curi][curj+1].v==binary || nodes[curi-1][curj+1].v==binary || nodes[curi+1][curj+1].v==binary || nodes[curi-1][curj+2].v==binary || nodes[curi+1][curj+2].v==binary)
					{right=1;cantgo++;}
				if(nodes[curi+2][curj].v==binary || nodes[curi+1][curj].v==binary || nodes[curi+1][curj-1].v==binary || nodes[curi+1][curj+1].v==binary || nodes[curi+2][curj-1].v==binary || nodes[curi+2][curj+1].v==binary)
					{down=1;cantgo++;}
				break;

			case 4:
				list[0]=1;
				list[1]=4;
				list[2]=3;
				list[3]=2;
				//list=[1,4,3,2];
				if(nodes[curi][curj-2].v==binary || nodes[curi][curj-1].v==binary || nodes[curi-1][curj-1].v==binary || nodes[curi+1][curj-1].v==binary || nodes[curi-1][curj-2].v==binary || nodes[curi+1][curj-2].v==binary)
					{left=1;cantgo++;}
				if(nodes[curi-2][curj].v==binary || nodes[curi-1][curj].v==binary || nodes[curi-1][curj-1].v==binary || nodes[curi-1][curj+1].v==binary || nodes[curi-2][curj-1].v==binary || nodes[curi-2][curj+1].v==binary)
					{up=1;cantgo++;}
				if(nodes[curi+2][curj].v==binary || nodes[curi+1][curj].v==binary || nodes[curi+1][curj-1].v==binary || nodes[curi+1][curj+1].v==binary || nodes[curi+2][curj-1].v==binary || nodes[curi+2][curj+1].v==binary)
					{down=1;cantgo++;}
				break;

			case 1:
				list[0]=1;
				list[1]=2;
				list[2]=4;
				list[3]=3;
				//list=[1,2,4,3];
				if(nodes[curi][curj-2].v==binary || nodes[curi][curj-1].v==binary || nodes[curi-1][curj-1].v==binary || nodes[curi+1][curj-1].v==binary || nodes[curi-1][curj-2].v==binary || nodes[curi+1][curj-2].v==binary)
					{left=1;cantgo++;}
				if(nodes[curi][curj+2].v==binary || nodes[curi][curj+1].v==binary || nodes[curi-1][curj+1].v==binary || nodes[curi+1][curj+1].v==binary || nodes[curi-1][curj+2].v==binary || nodes[curi+1][curj+2].v==binary)
					{right=1;cantgo++;}
				if(nodes[curi-2][curj].v==binary || nodes[curi-1][curj].v==binary || nodes[curi-1][curj-1].v==binary || nodes[curi-1][curj+1].v==binary || nodes[curi-2][curj-1].v==binary || nodes[curi-2][curj+1].v==binary)
					{up=1;cantgo++;}
				break;

			case 2:
				list[0]=1;
				list[1]=2;
				list[2]=3;
				list[3]=4;
				//list=[1,2,3,4];
				if(nodes[curi-2][curj].v==binary || nodes[curi-1][curj].v==binary || nodes[curi-1][curj-1].v==binary || nodes[curi-1][curj+1].v==binary || nodes[curi-2][curj-1].v==binary || nodes[curi-2][curj+1].v==binary)
					{up=1;cantgo++;}
				if(nodes[curi][curj+2].v==binary || nodes[curi][curj+1].v==binary || nodes[curi-1][curj+1].v==binary || nodes[curi+1][curj+1].v==binary || nodes[curi-1][curj+2].v==binary || nodes[curi+1][curj+2].v==binary)
					{right=1;cantgo++;}
				if(nodes[curi+2][curj].v==binary || nodes[curi+1][curj].v==binary || nodes[curi+1][curj-1].v==binary || nodes[curi+1][curj+1].v==binary || nodes[curi+2][curj-1].v==binary || nodes[curi+2][curj+1].v==binary)
					{down=1;cantgo++;}
				break;		
			}
			
		if(cantgo==3)
			{
			ttop--;
			curj=(int)(stackx[ttop]/thick);
			curi=(int)(stacky[ttop]/thick);
			move=stackm[ttop];
			
			
			
			paint.setColor(Color.WHITE);
			myrect.set(nodes[curi][curj].x,nodes[curi][curj].y, nodes[curi][curj].x+thick, nodes[curi][curj].y+thick);
			
			if(watchMazeGen)
				canvas = holder.lockCanvas(myrect);
			canvas.drawRect(myrect, paint);
			if(watchMazeGen)
				holder.unlockCanvasAndPost(canvas);
			
			if(ttop==0)
				{
				over=1;
				binary=0;
				move=1;
				
				//document.addEventListener("mousemove",play);
				
				nodes[2][2].v=2;
				answer.add(nodes[2][2]);
				
				paint.setColor(Color.BLUE);
				myrect.set(nodes[2][2].x,nodes[2][2].y, nodes[2][2].x+thick, nodes[2][2].y+thick);
				if(watchMazeGen)
				canvas = holder.lockCanvas(myrect);
				canvas.drawRect(myrect, paint);
				if(watchMazeGen)
				holder.unlockCanvasAndPost(canvas);
//				ctx.beginPath();
//				ctx.rect(nodes[2][2].x,nodes[2][2].y,thick,thick);
//				ctx.fillStyle="blue";
//				ctx.fill();
//				ctx.closePath();
				
				
				}
			}
		else
			{
			paint.setColor(Color.WHITE);
			myrect.set(nodes[curi][curj].x,nodes[curi][curj].y, nodes[curi][curj].x+thick, nodes[curi][curj].y+thick);
			if(watchMazeGen)
				canvas = holder.lockCanvas(myrect);
			
			canvas.drawRect(myrect, paint);
			if(watchMazeGen)
				holder.unlockCanvasAndPost(canvas);
			
			cantgo=3;
			if(up==1)
				{
				for(int i=0;i<cantgo;i++)
					if(list[i]==1)
						list[i]=list[cantgo-1];
				cantgo--;		
				}
			if(down==1)
				{
				for(int i=0;i<cantgo;i++)
					if(list[i]==3)
						list[i]=list[cantgo-1];
				cantgo--;		
				}
			if(left==1)
				{
				for(int i=0;i<cantgo;i++)
					if(list[i]==4)
						list[i]=list[cantgo-1];
				cantgo--;		
				}
			if(right==1)
				{
				for(int i=0;i<cantgo;i++)
					if(list[i]==2)
						list[i]=list[cantgo-1];
				cantgo--;		
				}

			ran=(int)(Math.random()*cantgo);
			move=list[ran];	
			//console.log(move+" "+cantgo+" "+list[0]+" "+list[1]+" "+list[2]+" "+list[3]);
			switch(move)
				{
				case 1:
					curi--;
					break;
				case 2:
					curj++;
					break;
				case 3:
					curi++;
					break;		
				case 4:
					curj--;
					break;
				}
			
			stackx[++ttop]=nodes[curi][curj].x;
			stacky[ttop]=nodes[curi][curj].y;
			stackm[ttop]=move;
				
			nodes[curi][curj].v=binary;
			
			paint.setColor(Color.WHITE);
			myrect.set(nodes[curi][curj].x,nodes[curi][curj].y, nodes[curi][curj].x+thick, nodes[curi][curj].y+thick);
			
			if(watchMazeGen)
				canvas = holder.lockCanvas(myrect);
			canvas.drawRect(myrect, paint);
			if(watchMazeGen)
				holder.unlockCanvasAndPost(canvas);
			
			}
			
		if(over!=0)
			setRunning(false);
		
		
			//repf=setTimeout(function(){frame()},1);
		/*
		try {
			sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		finally{}
		*/
	}

	
}