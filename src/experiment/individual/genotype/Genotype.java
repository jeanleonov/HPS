package experiment.individual.genotype;

import java.util.ArrayList;
import java.util.List;

public class Genotype {

	// ==============================| Class Genotype: |===
	private Genome[] genomes;
	private Boolean[] clonalities;
	private int id;

	private Genotype(Genome[] genomes, Boolean[] clonalities) {
		this.genomes = genomes;
		this.clonalities = clonalities;
	}

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (getClass() != obj.getClass())
			return false;
		Genotype gm = (Genotype) obj;
		if (gm.genomes.length != genomes.length)
			return false;
		for (int i = 0; i < genomes.length; i++) {
			if (!gm.genomes[i].equals(genomes[i]))
				return false;
			if (gm.clonalities[i] != clonalities[i])
				return false;
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

	public boolean isFemale() {
		for (Genome genome : genomes)
			if (!genome.isXGender())
				return false;
		return true;
	}

	public Genome[] getGenomes() {
		return genomes;
	}

	public Boolean[] getClonalities() {
		return clonalities;
	}

	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int i = 0; i < genomes.length; i++) {
			if (clonalities[i])
				buff.append("(");
			buff.append(genomes[i].toString());
			if (clonalities[i])
				buff.append(")");
		}
		return buff.toString();
	}

	// ==============================| GLOBAL: |===========

	static private List<Genotype> genotypes = new ArrayList<Genotype>();
	final static public byte UNDEF = -1;

	static public Genotype getGenotype(Genome[] genomes, Boolean[] booleans) {
		Genotype genotype = new Genotype(genomes, booleans);
		int index = genotypes.indexOf(genotype);
		if (index == -1) {
			genotype.id = genotypes.size();
			genotypes.add(genotype);
			return genotype;
		}
		return genotypes.get(index);
	}

	static public Genotype getGenotype(String str) throws Exception {
		List<Genome> genomes = new ArrayList<>(2);
		List<Boolean> clonalities = new ArrayList<>(2);
		boolean isBracketOpened = false;
		for (int i = 0; i < str.length(); isBracketOpened = false) {
			if (str.charAt(i) == '(') {
				if (isBracketOpened)
					throw new Exception("Can not parse genotype: \'" + str
							+ "\'");
				isBracketOpened = true;
				clonalities.add(Boolean.TRUE);
				i++;
			} else
				clonalities.add(Boolean.FALSE);
			genomes.add(new Genome(str.charAt(i++), str.charAt(i++)));
			if (isBracketOpened && str.charAt(i++) != ')')
				throw new Exception("Can not parse genotype: \'" + str + "\'");
		}
		return getGenotype(
				genomes.<Genome> toArray(new Genome[genomes.size()]),
				clonalities.<Boolean> toArray(new Boolean[clonalities.size()]));
	}

	static public Genotype getGenotypeById(int id) {
		return genotypes.get(id);
	}

	static public int getIdOf(Genotype genotype) {
		return genotypes.indexOf(genotype);
	}

	static public List<Genotype> getAll() {
		return genotypes;
	}
}