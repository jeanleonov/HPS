package experiment.individual.genotype;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Genotype implements Serializable {
// Important!  constructor of Genotype is PRIVATE
//   USE static methods for getting Genotype or Id of Genotype

	private static final long serialVersionUID = 1L;
//==============================| Class Genotype: |===
	Genome[] genomes;
	boolean[] clonalities;
	private int id;

	private Genotype (Genome[] genomes, boolean[] clonalities, int id) {
		this.genomes = genomes;
		this.clonalities = clonalities;
		this.id = id;
	}
	
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (getClass() != obj.getClass())
			return false;
		Genotype gm = (Genotype) obj;
		if(gm.genomes.length != genomes.length) return false;
		for(int i = 0; i < genomes.length; i++) {
			if(!gm.genomes[i].equals(genomes[i])) return false;
			if(gm.clonalities[i] != clonalities[i]) return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		return id;
	}

	public int getId() {
		return id;
	}

	// only for 2 Genomes in Genotype!!!
	public byte getGender(){
		if(genomes[0].gender == Genome.X  &&  genomes[1].gender == Genome.X)
			return Genome.X;
		if(genomes[0].gender == Genome.X  &&  genomes[1].gender == Genome.Y
		|| genomes[0].gender == Genome.Y  &&  genomes[1].gender == Genome.X)
			return Genome.Y;
		return Genome.UNDEF;
	}
	
	public boolean isFemale() {
		return getGender()==Genome.X;
	}

	public Genome[] getGenomes() {
		return genomes;
	}

	public boolean[] getClonalities() {
		return clonalities;
	}

	public String toString() {
		StringBuilder buff = new StringBuilder();
		for(int i = 0; i < genomes.length; i++) {
			if(clonalities[i]) buff.append("(");
			buff.append(genomes[i].toString());
			if(clonalities[i]) buff.append(")");
		}
		return buff.toString();
	}




	//==============================| GLOBAL: |===========

	static private List<Genotype> genotypes = new ArrayList<Genotype>();
	final static public byte UNDEF = -1;

	/* It serves as a constructor
	 *
	 */
	static public Genotype getGenotype(Genome[] genomes, boolean[] clonalities){
		boolean mustContinue;
		for (int i=0; i<genotypes.size(); i++){
			mustContinue=false;
			if (genomes.length != genotypes.get(i).genomes.length)	continue;
			for (int j=0; j<clonalities.length; j++)
				if (clonalities[j] != genotypes.get(i).clonalities[j]){
					mustContinue = true;
					break;
				}
			if (mustContinue)	continue;
			for (int j=0; j<genomes.length; j++)
				if (!genomes[j].equals(genotypes.get(i).genomes[j])){
					mustContinue = true;
					break;
				}
			if (mustContinue)	continue;
			return genotypes.get(i);
		}
		Genotype genotype = new Genotype(genomes, clonalities, genotypes.size());
		genotypes.add(genotype);
		return genotype;
	}

	/* It serves as a constructor by String as "xRyL", or "xRxR", or "(yL)xL" ...
	 *
	 */
	static public Genotype getGenotype(String str) throws Exception{
		int shift = 0;
		Genome[] genomes = new Genome[2];
		boolean[] clonalities = new boolean[2];
		if (str.charAt(0)=='('){
			if (str.charAt(3)!=')')
				throw new Exception("Can not parse genotype: \'"+str+"\'");
			clonalities[0] = true;
			if ((genomes[0] = parseGenome(str.charAt(1), str.charAt(2))) == null)
				throw new Exception("Can not parse genotype: \'"+str+"\'");
			shift = 2;
		}
		else{
			if ((genomes[0] = parseGenome(str.charAt(0), str.charAt(1))) == null)
				throw new Exception("Can not parse genotype: \'"+str+"\'");
		}
		if (str.charAt(2+shift)=='('){
			if (str.charAt(5+shift)!=')')
				throw new Exception("Can not parse genotype: \'"+str+"\'");
			clonalities[1] = true;
			if ((genomes[1] = parseGenome(str.charAt(3+shift), str.charAt(4+shift))) == null)
				throw new Exception("Can not parse genotype: \'"+str+"\'");
		}
		else{
			if ((genomes[1] = parseGenome(str.charAt(2+shift), str.charAt(3+shift))) == null)
				throw new Exception("Can not parse genotype: \'"+str+"\'");
		}
		return getGenotype(genomes, clonalities);
	}

	// only for 2 Genomes in Genotype!!!
	static private Genome parseGenome(char gender, char name) throws Exception{
		byte genderByte;
		Genome.GenomeName nameByte;
		if (gender == 'x' || gender == 'X')
			genderByte = Genome.X;
		else if (gender == 'y' || gender == 'Y')
			genderByte = Genome.Y;
		else
			throw new Exception("Can not parse genome: \'"+name+gender+"\'");
		if (name == 'l' || name == 'L')
			nameByte = Genome.GenomeName.L;
		else if (name == 'r' || name == 'R')
			nameByte = Genome.GenomeName.R;
		else
			throw new Exception("Can not parse genome: \'"+name+gender+"\'");
		return new Genome(genderByte, nameByte);
	}

	/* It serves as a constructor
	 * but return ID
	 */
	static public int getGenotypeId(Genome[] genomes, boolean[] clonalities){
		boolean mustContinue;
		for (int i=0; i<genotypes.size(); i++){
			mustContinue=false;
			if (genomes.length != genotypes.get(i).genomes.length)	continue;
			for (int j=0; j<clonalities.length; j++)
				if (clonalities[j] != genotypes.get(i).clonalities[j]){
					mustContinue = true;
					break;
				}
			if (mustContinue)	continue;
			for (int j=0; j<genomes.length; j++)
				if (genomes[j].equals(genotypes.get(i).genomes[j])){
					mustContinue = true;
					break;
				}
			if (!mustContinue)	return i;
		}
		Genotype genotype = new Genotype(genomes, clonalities, genotypes.size());
		genotypes.add(genotype);
		return genotypes.size()-1;
	}

	// get existed genotype
	static public Genotype getGenotypeById(int id){
		return genotypes.get(id);
	}

	// get existed ID
	static public int getIdOf(Genotype genotype){
		return genotypes.indexOf(genotype);
	}
	
	static public List<Genotype> getAll() {
		return genotypes;
	}
}