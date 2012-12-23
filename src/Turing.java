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
    	int tamanio = 600;
        ActorWorld world = new ActorWorld(new BoundedGrid<Actor>(2,tamanio));
        MaquinaTuring maquina= new MaquinaTuring(world,tamanio);
        
        world.show();
        
        Parser p = new Parser ("programa.txt");
        maquina.cargarTransiciones(p.transiciones);
       
        solicitarPaso (maquina);
    }
    
    public static void solicitarPaso (MaquinaTuring m){

        Scanner in = new Scanner (System.in);
        while (true)
        	m.paso(in.nextInt());
    }
}

class MaquinaTuring {
	ActorWorld world; 
	Transicion[] transiciones;
	int [] cinta; 
	int estado; 
	int posicion;
	int tamanio;
	Bug cabezal;
	
	MaquinaTuring (ActorWorld w, int t)
	{
		this.cinta = new int [t];
		this.tamanio = t; 
		this.world = w;
		this.cabezal = new Bug();
		world.add(this.cabezal);
	}
	
	void cargarTransiciones (Transicion [] t)
	{
		this.transiciones = t; 
	}
	
	boolean cargarCinta (int [] c)
	{
		if (this.tamanio < c.length)
			return false;
		
		this.posicion = (this.tamanio-c.length)/2;
		for (int i = 0;i < c.length; ++i)
			this.cinta[i+this.posicion] = c[i];
		
		return true;
	}
		
	Transicion buscarTransicion (int estado, int cinta)
	{
		for (int i=0; i<this.transiciones.length; ++i)
		{
			Transicion t = this.transiciones[i];
			if (t.valorMaquina==estado && t.valorCinta==cinta)
				return t;
		}
		return null; 
	}
	
	/**
	 * @param veces
	 */
	void paso (int veces)
	{
		for(int i = 0; i < veces; i++){
			Transicion t = buscarTransicion (this.estado,this.cinta[this.posicion]);
			if (t==null)
				fin ();
			else 
			{
			this.estado = t.nuevoValorMaquina;
			this.cinta [this.posicion] = t.nuevoValorCinta;
			if (t.direccion == -1) 
				this.posicion --;
			else 
				this.posicion ++;
			}
			actualizar();
		}
	}
	
	void fin ()
	{
		System.out.println("FIN");
		
		for(int i = 0; i<this.tamanio; ++i)
			System.out.print(this.cinta[i]+ " ");
		System.out.println();
	}
	
	void actualizar ()
	{
		this.cabezal.moveTo(new Location(0, this.posicion));
		
	}
}

