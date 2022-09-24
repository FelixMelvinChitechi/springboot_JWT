package com.superjava.product.api;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Id;

@Entity
@Table(name="products")
public class Product {

	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	private Integer Id;
	
	@Column(nullable = false, length = 128)
	@Length(min=5, max=128)
	private String name;
	
	private float price;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
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
	
	
}
