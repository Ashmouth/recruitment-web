package fr.d2factory.libraryapp.member;

public class Resident extends Member {
	
	private float COSTPERDAY = 0.10f;
	private float COSTPERDAYLATE = 0.20f;
	private int DAYLIMITONBORROWING = 60;
	
	public Resident(String name, float wallet) {
		super(name, wallet);
	}

	@Override
	public void payBook(int numberOfDays) {
		if (numberOfDays > DAYLIMITONBORROWING) {
			wallet -= (DAYLIMITONBORROWING * COSTPERDAY);
			wallet -= ((numberOfDays - DAYLIMITONBORROWING) * COSTPERDAYLATE);
		} else {
			wallet -= (numberOfDays * COSTPERDAY);
		}
	}

	@Override
	public int getDAYLIMITONBORROWING() {
		return DAYLIMITONBORROWING;
	}

}
