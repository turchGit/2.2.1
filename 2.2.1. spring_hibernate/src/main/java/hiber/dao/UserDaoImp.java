package hiber.dao;

import hiber.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {
      Transaction transaction = null;
      try(Session session = sessionFactory.openSession()) {
         transaction = session.beginTransaction();
         session.save(user.getCar());
         session.save(user);
         transaction.commit();
      }catch (Exception e){
         if (transaction!=null) {
            transaction.rollback();
         }
      }
   }

   @Override
   public User getUserByParams(String model, int series) {
      User result;
      try (Session session = sessionFactory.openSession()) {
         String hql = "select u " +
                 "from User u " +
                 "where u.car.model like :model " +
                 "and u.car.series like :series ";
         result = (User) session.createQuery(hql)
                 .setParameter("model", model)
                 .setParameter("series", series)
                 .uniqueResult();
      }
      return result;

   }



   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }



}
