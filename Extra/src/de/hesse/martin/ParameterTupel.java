package de.hesse.martin;

public class ParameterTupel {
	public int[] tupel;
	
	public ParameterTupel(int[] tupel) {
		this.tupel = tupel;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ParameterTupel){
			ParameterTupel tupel = (ParameterTupel) obj;
			if (this.tupel.length != tupel.tupel.length) return false;
			for (int i = 0; i < this.tupel.length; i++) {
				if (this.tupel[i] != tupel.tupel[i]) return false;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		for (int i = 0; i < tupel.length; i++) {
			hashCode = 27865 * hashCode + tupel[i];
		}
		return hashCode;
	}

}
