package com.formacionbdi.springboot.app.item.models.service.implementations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.models.Producto;
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

}
