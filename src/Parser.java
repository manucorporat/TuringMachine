
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
	 * Imprime una explicaci—n es lenguaje natural de la regla.
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
	private final int maxRules = 200;
	public final static char charA = 'a';
	public final static char charB = 'b';
	public final static char charC = 'c';
	public final static char charX = 'x';
	public final static char charY = 'y';
	public final static char charANY = '*';
	public final static char charEND = 'h';
	public final static char charBLANK = '_';

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
		System.out.println("Parsing \""+filename+"\"");
		
		this.nuRules = 0;
		try {
			Scanner sc = new Scanner(new File(filename));
			
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
						System.out.println("Error at line "+(this.nuRules+1));
						throw e;
					}
					++this.nuRules;
				}
			}
			
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			return false;
		}
		return true;
	}
	
	
	/**
	 * Parsea un cada parte de la linea.
	 * Retorna un int.
	 * @param subzone texto a ser parseado.
	 * @param min minimo valor del int para validacion.
	 * @param max maximo valor del int para validacion.
	 */
	protected int parseZone(String subZone, int min, int max)
	{
		char first = subZone.charAt(0);
		switch(first) {
		
		// ALIAS
		case charA: return TuringMachine.valueA;
		case charB: return TuringMachine.valueB;
		case charC: return TuringMachine.valueC;		
		case charX: return TuringMachine.valueX;
		case charY: return TuringMachine.valueY;
		
		// SPEACIL CHARCHTS
		case charANY:
			return TuringMachine.valueANY;
			
		case charEND:
			return TuringMachine.valueEND;
			
		case charBLANK:
			return TuringMachine.valueBLANK;
			
		//NUMBER
		default:
			int r = Integer.parseInt(subZone);
			if(r < min || r > max)
				throw new IllegalStateException("Out of range ["+min+", "+max+"]");
			
			return r;
		}
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
		
		t.machineState	= parseZone(zones[0], 0, 10000);
		t.tapeSymbol	= parseZone(zones[1], 0, 10000);
		t.newState		= parseZone(zones[2], 0, 10000);
		t.newSymbol		= parseZone(zones[3], 0, 10000);
		t.direction		= parseZone(zones[4], -1, 1);
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

