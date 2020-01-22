package com.formacionbdi.springboot.app.item.models.service.implementations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.commons.models.entities.Producto;
import com.formacionbdi.springboot.app.item.models.service.IItemService;

@Service("serviceRestTemplate")
public class ItemServiceImpl implements IItemService{

	@Autowired
	private RestTemplate clienteRest;
	
	private static final String BASE_URL_PRODUCTOS = "http://servicio-productos/";
	
	@Override
	public List<Item> findAll() {
		List<Producto> productos = Arrays.asList(clienteRest.getForObject(BASE_URL_PRODUCTOS+"listar", Producto[].class));
		return productos.stream().map( p -> new Item(p, 1) ).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());
		Producto producto = clienteRest.getForObject(BASE_URL_PRODUCTOS+"ver/{id}", Producto.class, pathVariables);
		return new Item(producto, cantidad);
	}

	@Override
	public Producto save(Producto producto) {
		HttpEntity<Producto> body = new HttpEntity<Producto>(producto);
		ResponseEntity<Producto> response = clienteRest.exchange(BASE_URL_PRODUCTOS+"crear", HttpMethod.POST, body, Producto.class);
		Producto productoResponse = response.getBody();
		return productoResponse;
	}

	@Override
	public Producto update(Producto producto, Long id) {
		HttpEntity<Producto> body = new HttpEntity<Producto>(producto);
		Map<String, String> pathVariable = new HashMap<String, String>();
		pathVariable.put("id", id.toString());
		ResponseEntity<Producto> response = clienteRest.exchange(BASE_URL_PRODUCTOS+"editar/{id}", HttpMethod.PUT, body, Producto.class, pathVariable);
		Producto productoResponse = response.getBody();
		return productoResponse;
	}

	@Override
	public void delete(Long id) {
		Map<String, String> pathVariable = new HashMap<String, String>();
		pathVariable.put("id", id.toString());
		clienteRest.delete(BASE_URL_PRODUCTOS+"eliminar/{id}", pathVariable);
	}

}
