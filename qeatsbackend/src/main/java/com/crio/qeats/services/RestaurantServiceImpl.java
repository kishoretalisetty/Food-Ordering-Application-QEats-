
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.services;

import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.repositoryservices.RestaurantRepositoryService;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RestaurantServiceImpl implements RestaurantService {

  private final Double peakHoursServingRadiusInKms = 3.0;
  private final Double normalHoursServingRadiusInKms = 5.0;
  
  @Autowired
  private RestaurantRepositoryService restaurantRepositoryService;


  // TODO: CRIO_TASK_MODULE_RESTAURANTSAPI - Implement findAllRestaurantsCloseby.
  // Check RestaurantService.java file for the interface contract.
  @Override
  public GetRestaurantsResponse findAllRestaurantsCloseBy(
      GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) {
       
        double latitude=getRestaurantsRequest.getLatitude();
        double longitude=getRestaurantsRequest.getLongitude();
        String searchFor=getRestaurantsRequest.getSearchFor();

        // For peak hours: 8AM - 10AM, 1PM-2PM, 7PM-9PM
         if((currentTime.isAfter(LocalTime.of(7,59)) && currentTime.isBefore(LocalTime.of(10,1))) || 
         (currentTime.isAfter(LocalTime.of(12,59)) && currentTime.isBefore(LocalTime.of(15,1)))    ||
         (currentTime.isAfter(LocalTime.of(18,59)) && currentTime.isBefore(LocalTime.of(21,1)))){
         List<Restaurant> restaurantsradius3km =  restaurantRepositoryService.findAllRestaurantsCloseBy(latitude, longitude, currentTime, peakHoursServingRadiusInKms);
         //System.out.println("#2 -> "+ restaurantsradius3km);
        // System.out.println("-----------------------");
         return new GetRestaurantsResponse(restaurantsradius3km);
         }
         else{
         List<Restaurant> restaurantsradius5km = restaurantRepositoryService.findAllRestaurantsCloseBy(latitude, longitude, currentTime, normalHoursServingRadiusInKms);
        // System.out.println("#2 -> "+ restaurantsradius5km);
        return new GetRestaurantsResponse(restaurantsradius5km);

      }
      // // for(int i=0; i<restaurants.size(); i++){
      // //   List<String> attributesList=restaurants.get(i).getAttributes();
      // //   for(int j=0; j<attributesList.size(); j++){
      // //     if(attributesList.get(j).equals(searchFor)){
      // //       return new GetRestaurantsResponse(restaurants.get(i).getLatitude(), restaurants.get(i).getLongitude(), searchFor);
      // //     }
      // //   }
      // // }
      // return null;
  }


}

