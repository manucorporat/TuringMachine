/* 
 * AP(r) Computer Science GridWorld Case Study:
 * Copyright(c) 2005-2006 Cay S. Horstmann (http://horstmann.com)
 *
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * @author Cay Horstmann
 */

import info.gridworld.actor.ActorWorld;
import info.gridworld.actor.Bug;
import info.gridworld.actor.Rock;
import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Location;
import info.gridworld.actor.Actor;
import java.util.Scanner;
/**
 * This class runs a world that contains a bug and a rock, added at random
 * locations. Click on empty locations to add additional actors. Click on
 * populated locations to invoke methods on their occupants. <br />
 * To build your own worlds, define your own actors and a runner class. See the
 * BoxBugRunner (in the boxBug folder) for an example. <br />
 * This class is not tested on the AP CS A and AB exams.
 */
public class Turing
{
    public static void main(String[] args)
    {    	
    	final int tamanio = 40;
        int cinta[] = {0,0, 0, 0};

        MaquinaTuring maquina= new MaquinaTuring("increment", tamanio);
        maquina.cargarCinta(cinta);
        
        solicitarPaso (maquina);
        
        //ActorWorld world = new ActorWorld(new BoundedGrid<Actor>(2,tamanio));
        //world.show();
    }
    
    public static void solicitarPaso (MaquinaTuring m){

        Scanner in = new Scanner (System.in);
        while (true)
        	m.paso(in.nextInt());
    }
}
