package com.artigo.dota.service;

public interface ProductDetailsService {

    boolean reduceProductQuantity(long productDetailsId, int orderedQuantity);
}
