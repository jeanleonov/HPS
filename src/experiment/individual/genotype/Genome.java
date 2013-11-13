package experiment.individual.genotype;

public class Genome {
	
	final static public boolean	X = true,
								Y = false;
	
	private boolean xGender;
	private char letter;
	
	public Genome(Character gender, Character letter) throws Exception{
		if (gender.charValue() == 'x' || gender.charValue() == 'X')
			xGender = Genome.X;
		else if (gender.charValue() == 'y' || gender.charValue() == 'Y')
			xGender = Genome.Y;
		else
			throw new Exception("Can not parse genome: \'"+gender+letter+"\'");
		if (Character.isLetter(letter))
			this.letter = letter;
		else
			throw new Exception("Can not parse genome: \'"+gender+letter+"\'");
	}
	
	public Genome(boolean isXGender, char letter){
		this.xGender = isXGender;
		this.letter = letter;
	}

	public boolean isXGender() {
		return xGender;
	}

	public char getLetter() {
		return letter;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (getClass() != obj.getClass())
			return false;
		return xGender == ((Genome)obj).xGender
			&&  letter == ((Genome)obj).letter;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(xGender ? 'x' : 'y');
		str.append(letter);
		return str.toString();
	}
}
