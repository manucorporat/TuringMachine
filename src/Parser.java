
import java.io.*;
import java.util.Scanner;
import java.io.File;



class Transicion
{
	public int valorMaquina; // q
	public int valorCinta; // e
	
	public int nuevoValorMaquina; // p
	public int nuevoValorCinta; // f
	public int direccion; // m
	
	Transicion()
	{
		valorMaquina = 0;
		valorCinta = 0;
		nuevoValorMaquina = 0;
		nuevoValorCinta = 0;
		direccion = 0;
	}
}


class Parser
{
	public final static int charComodin = '*';
	public final static int charFin = 'h';
	public final static int charBlanco = 'b';


	
	public Transicion [] transiciones;
	public int nuTransiciones;
	public boolean hayTransiciones;
	public boolean hayCinta;

	
	Parser(String filename)
	{
		this.hayTransiciones = true;
		this.hayCinta = false;
		
		this.transiciones = new Transicion[200];
		for(int i = 0; i < 200; ++i)
			this.transiciones[i] = new Transicion();
		
		this.nuTransiciones = 0;
		
		
		readFile(filename);
	}
	

	protected boolean readFile(String filename)
	{
		System.out.println("Parsing \""+filename+"\"");

		try{
			Scanner sc = new Scanner(new File(filename));

			while (sc.hasNextLine()) {
				
				String strLine = sc.nextLine();
				Transicion t = this.transiciones[this.nuTransiciones];
				
				if(parseLine(strLine, t))
					print(t);
				else {
					System.out.println("Error reading line "+(this.nuTransiciones+1));
					return false;
				}
				++this.nuTransiciones;
			}
			
		}catch (Exception e){ //Catch exception if any
			System.err.println("Error: " + e.getMessage());
			return false;
		}
		return true;
	}
	
	
	protected int parseZone(String subZone, int min, int max)
	{
		char first = subZone.charAt(0);
		switch(first) {
		case charComodin:
			return MaquinaTuring.valorComodin;
			
		case charFin:
			return MaquinaTuring.valorFin;
			
		case charBlanco:
			return MaquinaTuring.valorBlanco;
			
		default:
			int r = Integer.parseInt(subZone);
			if(r < min || r > max)
				throw new IllegalStateException("Out of range ["+min+", "+max+"]");
			
			return r;
		}
	}
	
	
	protected boolean parseLine(String line, Transicion t)
	{		
		String[] zonas = line.split("\\s+");

		if(zonas.length < 5)
			return false;
		
		t.valorMaquina = parseZone(zonas[0], 0, 100000);
		t.valorCinta = parseZone(zonas[1], 0, 1);
		t.nuevoValorMaquina = parseZone(zonas[2], 0, 10000);
		t.nuevoValorCinta = parseZone(zonas[3], 0, 6);
		t.direccion = parseZone(zonas[4], -1, 1);
		
		return true;
	}
	
	
	protected void print(Transicion t)
	{
		System.out.printf("%3d %3d %3d %3d %3d \n",
				t.valorMaquina,
				t.valorCinta,
				t.nuevoValorMaquina,
				t.nuevoValorCinta,
				t.direccion);
	}
}

