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

import java.util.Scanner;


public class Turing
{
    public static void main (String[] args)
    {   
        Scanner in = new Scanner (System.in);
        System.out.print("Introduce el programa: ");
        
        // PARSE FILENAME
        String filename = in.nextLine();
        TuringMachine machine = new TuringMachine(filename, 50);
        
        // PARSE TAPE
        System.out.print("Introduce la cinta: ");
        String cinta = in.nextLine();
		String[] numbers = cinta.split("\\s+");
		int [] tape = new int[numbers.length];
		for(int i = 0; i < numbers.length; ++i)
			tape[i] = Integer.parseInt(numbers[i]);

        machine.loadTape(tape);
        
        GridManager manager = new GridManager(machine);
    	manager.show();
    }
    
    
    public static void requestStep (TuringMachine m){

        Scanner in = new Scanner (System.in);
        while (true) {
        	int c = in.nextInt();
        	m.step(c);
        }
    }
}
