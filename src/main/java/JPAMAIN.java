

import domain.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.sql.Date;
import java.util.List;


public class JPAMAIN {
    public static void main(String[] args) {

        long start = 0;
        long end = 0;
        long resultTime = 0;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("somang");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            start = System.nanoTime();

            Team teamA = new Team();
            teamA.setId(1L);
            teamA.setName("teamA");
            em.persist(teamA);

            Product productA = new Product();
            productA.setName("productA");
            productA.setPrice(1_290_000);
            productA.setStockAmount(2_000);
            em.persist(productA);

            Team teamB = new Team();
            teamB.setId(2L);
            teamB.setName("teamB");
            em.persist(teamB);

            Product productB = new Product();
            productB.setName("productB");
            productB.setPrice(500_000);
            productB.setStockAmount(12_000);
            em.persist(productB);

            Team teamC = new Team();
            teamC.setId(3L);
            teamC.setName("teamC");
            em.persist(teamC);

            Product productC = new Product();
            productC.setName("productC");
            productC.setPrice(21_500_000);
            productC.setStockAmount(300);
            em.persist(productC);

            Team teamD = new Team();
            teamD.setId(4L);
            teamD.setName("teamD");
            em.persist(teamD);

            Product productD = new Product();
            productD.setName("productD");
            productD.setPrice(500);
            productD.setStockAmount(100_000);
            em.persist(productD);


            for (int i = 1; i <= 100; i++) {
                Member member = new Member();

                String username = i > 9 ? String.valueOf(i) : '0' + String.valueOf(i);


                member.setUsername("member" + username);
                member.setAge(
                        (int) (Math.random() * 100) + 1
                );
                em.persist(member);

                Order order = new Order();
                String city = "city" + i;
                String street = "street" + i;
                String zipcode = "zipcode" + i;

                order.setAddress(new Address(city, street, zipcode));
                System.err.println("order.getOrderAmount() = " + order.getOrderAmount());

                order.setOrderAmount(
                        (int) (Math.random() * 10_000) + 1);
                em.persist(order);

                Product product = new Product();


                if (i >= 0 && i <= 25) {
                    member.setTeam(teamA);

                    order.setProduct(productA);
                } else if (i >= 26 && i <= 50) {
                    member.setTeam(teamB);
                    order.setProduct(productB);
                } else if (i >= 51 && i <= 75) {
                    member.setTeam(teamC);
                    order.setMember(member);
                    order.setProduct(productC);
                } else {
                    member.setTeam(teamD);
                    order.setProduct(productD);
                }
            }

            em.flush();
            em.clear();

            Member member = new Member();
            member.setId(107L);



            System.out.println("========================");

            List<Object[]> resultList = em.createQuery(
                    "select " +
                            "   (case when m.age <= 10 then '학생요금' " +
                            "        when m.age >= 60 then '경로요금' " +
                            "        else '일반요금'" +
                            "   end), m " +
                            "from Member m " +
                            "order by m.username desc")
                    .getResultList();

            for (Object[] o : resultList) {
                String cost = (String) o[0];
                Member memberTemp = (Member) o[1];

                System.out.println("cost = " + cost + ", member = " + memberTemp);
            }



            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("e.getLocalizedMessage() = " + e.getLocalizedMessage());
            tx.rollback();
        } finally {
            em.close();
            end = System.nanoTime();
            resultTime = end - start;
            System.out.println("=========================");
            System.out.print("걸린 시간 : ");
            System.out.print((double) resultTime / 1000000000);
            System.out.println("초");

        }
        emf.close();


    }
}