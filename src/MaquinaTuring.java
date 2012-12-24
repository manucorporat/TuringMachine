import info.gridworld.actor.ActorWorld;


class MaquinaTuring
{
	final static int valorComodin = -4;
	final static int valorFin = -3;
	final static int valorBlanco = -2;

	
	ActorWorld world; 
	Transicion[] transiciones;
	int [] cinta; 
	int estado; 
	int posicion;
	int tamanio;
	
	MaquinaTuring (String filename, int t)
	{
		this.cinta = new int [t];
		for(int i = 0; i < t; ++i)
			this.cinta[i] = valorBlanco;
		
		this.tamanio = t; 
		this.estado = 0;
		
        Parser p = new Parser (filename);
        if(p.hayTransiciones)
        	cargarTransiciones(p.transiciones);
        if(p.hayCinta) {
        	//cargarCinta(p.cinta);
        }   
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
		for (int i = 0; i < this.transiciones.length; ++i) {
			Transicion t = this.transiciones[i];
			if ((t.valorMaquina==estado || t.valorMaquina==valorComodin) &&
				(t.valorCinta==cinta || t.valorCinta==valorComodin))
				return t;
		}
		return null; 
	}
	
	
	void paso (int veces)
	{
		for(int i = 0; i < veces; i++)
		{
			Transicion t = buscarTransicion(this.estado, this.cinta[this.posicion]);
			
			if (t == null) {
				fin ();
				break;
			
			}else 
			{				
				if(t.nuevoValorCinta != valorComodin)
					this.cinta[this.posicion] = t.nuevoValorCinta;
				
				if (t.direccion == -1) 
					this.posicion--;
				else if(t.direccion == 1)
					this.posicion++;
				
				if(t.nuevoValorMaquina == valorFin) {
					fin();
					break;
					
				}else if(t.nuevoValorMaquina != valorComodin)
					this.estado = t.nuevoValorMaquina;
			}
			actualizar();
		}
	}
	
	
	void fin ()
	{
		this.estado = 0;
		System.out.println("FIN");
		
		//for(int i = 0; i < this.tamanio; ++i)
		//	System.out.print(this.cinta[i]+" ");
		System.out.println();
	}
	
	
	void actualizar ()
	{
		for(int e = 0; e < this.tamanio; ++e) {
			if(this.cinta[e] == -2)
				System.out.print("_ ");
			else
				System.out.print(this.cinta[e]+" ");

		}
		System.out.println();

		//this.cabezal.moveTo(new Location(0, this.posicion));
		
	}
}

