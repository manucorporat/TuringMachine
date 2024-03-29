/*
 * TuringMachine
 *
 * Copyright (c) 2012 Manuel Martínez-Almeida
 * Copyright (c) 2012 Jose Maria Pinilla
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.util.Scanner;
import java.io.File;

class Rule
{
	public int machineState; // q
	public int tapeSymbol; // e
	
	public int newState; // p
	public int newSymbol; // f
	public int direction; // m
	
	/**
	 * Inicializa un objeto de tipo Rule, con los valores predefinidos.
	 */
	Rule()
	{
		this.machineState = 0;
		this.tapeSymbol = 0;
		this.newState = 0;
		this.newSymbol = 0;
		this.direction = 0;
	}
	
	
	/**
	 * Imprime en consola, los valores de la regla (Rule).
	 * <estado actual> <simbolo> <nuevo estado> <nuevo simbolo> <direccion>
	 */
	public void print()
	{
		System.out.printf("%3d %3d %3d %3d %3d \n",
				this.machineState,
				this.tapeSymbol,
				this.newState,
				this.newSymbol,
				this.direction);
	}
	
	
	/**
	 * Imprime una explicación es lenguaje natural de la regla.
	 */
	public void explain()
	{
		if(this.machineState == TuringMachine.valueANY)
			System.out.print("Con cualquier estado");
		else {
			System.out.print("Con estado ");
			TuringMachine.printValue(this.machineState);
		}
		if(this.tapeSymbol == TuringMachine.valueANY)
			System.out.print(" y cualquier simbolo");
		else {
			System.out.print(" y simbolo ");
			TuringMachine.printValue(this.tapeSymbol);
		}
		
		if(this.newState != TuringMachine.valueANY) {
			System.out.print(", cambiamos el estado a ");
			TuringMachine.printValue(this.newState);
		}
		
		
		if(this.newSymbol != TuringMachine.valueANY) {
			if(this.newState != TuringMachine.valueANY)
				System.out.print(" y el simbolo por ");
			else
				System.out.print(", cambiamos el simbolo por ");

			TuringMachine.printValue(this.newSymbol);
		}


		if(this.direction == -1)
			System.out.print(". Nos movemos a la izquierda.");
		else if(this.direction == 1)
			System.out.print(". Nos movemos a la derecha.");
		
		System.out.println();		
	}
}


class Parser
{
	public final static char charA = 'a';
	public final static char charB = 'b';
	public final static char charC = 'c';
	public final static char charD = 'd';
	public final static char charX = 'x';
	public final static char charY = 'y';
	public final static char charANY = '*';
	public final static char charEND = 'h';
	public final static char charBLANK = '_';
	
	private final int maxRules = 200;


	protected Rule [] rules;
	protected int nuRules;

	
	/**
	 * Inicializa un objeto de tipo parser.
	 * @param filename el programa a parsear.
	 */
	Parser() {
		// allocate array of references
		this.rules = new Rule[maxRules];
	}
	
	Parser(String filename)
	{
		this();
		readFile(filename);
	}
	
	
	/**
	 * Retorna true si el programa contenia alguna regla valida.
	 */
	public boolean hasRules()
	{
		return (this.nuRules > 0);
	}

	
	boolean postValidation()
	{
		for(int i = 0; i < this.nuRules-1; ++i) {
			for(int w = i+1; w < this.nuRules; ++w) {
				
				Rule a = this.rules[i];
				Rule b = this.rules[w];
				if(a.machineState == b.machineState &&
				a.tapeSymbol == b.tapeSymbol)
					return false;
			}
		}
		return true;
	}
	
	/**
	 * Retorna un array con todas la reglas parseadas.
	 */
	public Rule[] getRules()
	{
		return this.rules;
	}
	
	
	/**
	 * Retorna el numero de reglas parseadas.
	 */
	public int getNumberOfRules()
	{
		return this.nuRules;
	}
	

	/**
	 * Parsea el archivo especificado.
	 * @param filename url del archivo a ser parseado.
	 * @return true si el parseado se completo 
	 */
	public boolean readFile(String filename)
	{		
		this.nuRules = 0;
		try {
			Scanner sc = new Scanner(new File(filename));
			System.out.println("Parsing \""+filename+"\"");
			while (sc.hasNextLine()) {
				
				// get line
				String strLine = sc.nextLine();
				
				// ignoring comments
				if(strLine.length() > 0 && strLine.charAt(0) != '/') {
					
					// not enough memory, the array is too short.
					if(this.nuRules >= maxRules)
						throw new OutOfMemoryError("Too many lines to parse.");
					
					// allocate rule
					Rule t = new Rule();
					this.rules[this.nuRules] = t;
					
					// try to parse line
					try {
						parseLine(strLine, t);
						print(t);
					} catch(Exception e) {
						System.out.println("Error at line "+(this.nuRules+1)+".");
						throw e;
					}
					++this.nuRules;
				}
			}
			
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			return false;
		}
		
		if(!postValidation()) {
			System.out.println("Error: Duplicated rule.");
			return false;
		}
		System.out.println(this.nuRules+" rules parsed succesfully.");
		return true;
	}
	
	
	/**
	 * Parsea un cada parte de la linea.
	 * Retorna un int.
	 * @param subzone texto a ser parseado.
	 * @param min minimo valor del int para validacion.
	 * @param max maximo valor del int para validacion.
	 */
	public static int parseZone(String subZone, int min, int max, boolean allowHash)
	{
		if(subZone.length() == 1) {
			char first = subZone.charAt(0);
			switch(first) {
			// ALIAS
			case charA: return TuringMachine.valueA;
			case charB: return TuringMachine.valueB;
			case charC: return TuringMachine.valueC;
			case charD: return TuringMachine.valueD;
			case charX: return TuringMachine.valueX;
			case charY: return TuringMachine.valueY;
			// SPEACIL CHARS
			case charANY:
				return TuringMachine.valueANY;
				
			case charEND:
				return TuringMachine.valueEND;
				
			case charBLANK:
				return TuringMachine.valueBLANK;
				
			default: break;
			}
		}
		
		int r;
		try {
			// Trying to parse int
			r = Integer.parseInt(subZone);
			if(r < min || r > max)
				throw new IllegalStateException("Out of range ["+min+", "+max+"]");
			
		} catch(NumberFormatException  e) {
			// If string is not a int,
			// we can return the string's hash if allowed.
			if(allowHash)
				r = subZone.hashCode();
			else
				throw e;
		}
		return r;
	}
	
	
	/**
	 * Parsea un linea.
	 * @param line, linea a parsear.
	 * @param t, referencia a la regla a rellenar.
	 */
	protected void parseLine(String line, Rule t)
	{
		// Split the line using a regular expresion.
		String[] zones = line.split("\\s+");
		
		// We need 5 "arguments".
		if(zones.length < 5)
			throw new IllegalStateException("Missing 5-tuplas rule.");
		
		t.machineState	= parseZone(zones[0], TuringMachine.valueSTART, 10000, true);
		t.tapeSymbol	= parseZone(zones[1], 0, 10000, false);
		t.newState		= parseZone(zones[2], TuringMachine.valueSTART, 10000, true);
		t.newSymbol		= parseZone(zones[3], 0, 10000, false);
		t.direction		= parseZone(zones[4], -1, 1, false);
	}

	
	/**
	 * Usado para imprimir informacion mientras un programa es parseado.
	 */
	public void print(Rule t)
	{
		t.print();
		//t.explain();
	}
}

