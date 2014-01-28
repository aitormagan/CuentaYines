package es.aitormagan.android.cuentayines;

import java.io.Serializable;

public class Product implements Serializable {

	private static final long serialVersionUID = 1L;
	private transient static final int MAX_COUNT = 99;
	private String name;
	private float price;
	private int people;
	private int count;

	public Product(String name, float price, int people) {
		this.name = name;
		this.price = price;
		this.people = people;
		this.count = 0;
	}

	public Product(String name, float price) {
		this(name, price, 1);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getPeople() {
		return people;
	}

	public void setPeople(int people) {
		this.people = people;
	}

	public int getCount() {
		return count;
	}

	public void incrementCount() {
		if (count < MAX_COUNT)
			this.count++;
	}

	public void decreaseCount() {
		if (this.count > 0)
			this.count--;
	}

	public void setCountToZero() {
		this.count = 0;
	}

	public float getTotalPrice() {
		return count * price / people;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (name.hashCode() * people * (int) price);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Product){
			Product other = (Product) obj;
			return other.name.equals(this.name) && 
					other.price == this.price && 
					other.people == this.people;
		}

		return false;
	}



}
