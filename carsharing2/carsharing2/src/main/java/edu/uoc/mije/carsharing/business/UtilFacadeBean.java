package edu.uoc.mije.carsharing.business;

import java.util.*;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor;

import edu.uoc.mije.carsharing.integration.CityJPA;
import edu.uoc.mije.carsharing.integration.MessageJPA;
import edu.uoc.mije.carsharing.integration.TripJPA;
import edu.uoc.mije.carsharing.integration.util.ExampleModel;
import edu.uoc.mije.carsharing.integration.util.ExampleModel1;

@Stateless
public class UtilFacadeBean implements UtilFacadeRemote{

	@PersistenceContext(unitName = "CarSharing")
	private EntityManager entman;
	
	public UtilFacadeBean() {
		// TODO Auto-generated constructor stub
	}
	
	@Resource(mappedName="java:jboss/postgresDS") DataSource dataSource;
	
	@Override
	public void bootStrapModel() throws Exception {
		
		System.out.println("*******************");
		System.out.println(dataSource);
		System.out.println("*******************");
		
		
		CityJPA madrid,barcelona,malaga;
		if( (madrid=entman.find(CityJPA.class, "madrid")) == null){
			entman.persist(madrid=new CityJPA("madrid"));
		}
		if( (barcelona=entman.find(CityJPA.class, "barcelona")) == null){
			entman.persist(barcelona=new CityJPA("barcelona"));
		}
		if( (malaga=entman.find(CityJPA.class, "malaga")) == null){
			entman.persist(malaga=new CityJPA("malaga"));
		}	
	}
	
	public void loadModel( int exampleId ) throws Exception{
		
		ExampleModel1 model = new ExampleModel1();
		
		runModel(model);
	}
	
	private void runModel( ExampleModel model) throws Exception{
		model.cleanModel(entman);
		entman.flush();
		
		model.loadExampleModel(entman);
		entman.flush();
		
		model.validateModel(entman);
		entman.flush();
	}

	@Override
	public Collection<CityJPA> listCities() {
		return entman.createQuery("from CityJPA c", CityJPA.class).getResultList();
	}
	
	public TripJPA findTrip(int id){
		return entman.createQuery("from TripJPA c where c.id=:id", TripJPA.class)
				.setParameter("id", id)
				.getSingleResult();
	}
}
