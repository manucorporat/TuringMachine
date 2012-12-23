
import java.io.*;

class Transicion
{
	public int valorMaquina; // q
	public int valorCinta; // e
	
	public int nuevoValorMaquina; // p
	public int nuevoValorCinta; // f
	public int direccion; // m
}


class Parser
{
	public Transicion [] transiciones;
	public int nuTransiciones;
	
	Parser(String filename)
	{
		transiciones = new Transicion[200];
		nuTransiciones = 0;
		
		
		
		 try{
			  FileInputStream fstream = new FileInputStream(filename);
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  
			  String strLine;
			  while ((strLine = br.readLine()) != null)
			  {  
				  
				  Transicion t = transiciones[nuTransiciones];
				  if(parseLine(strLine, t))
					  print(t);
				  else {
					  System.out.println("Error reading line "+nuTransiciones+1);
					  return;
				  }  
				  ++nuTransiciones;
			  }
			  
			  //Close the input stream
			  in.close();
			  
		}catch (Exception e){ //Catch exception if any
			  System.err.println("Error: " + e.getMessage());
		}
	}
	
	
	protected boolean parseLine(String line, Transicion t)
	{
		String[] zonas = line.split(" ");
		
		if(zonas.length < 5)
			return false;
		
		t.valorMaquina = Integer.parseInt(zonas[0]);
		t.valorCinta = Integer.parseInt(zonas[1]);
		t.nuevoValorMaquina = Integer.parseInt(zonas[2]);
		t.nuevoValorCinta = Integer.parseInt(zonas[3]);
		t.direccion = Integer.parseInt(zonas[4]);

		return true;
	}
	
	protected void print(Transicion t)
	{
		System.out.printf("%d %d %d %d %d",
				t.valorMaquina,
				t.valorCinta,
				t.nuevoValorMaquina,
				t.nuevoValorCinta,
				t.direccion);
	}
}

