

import domain.*;

import javax.persistence.*;
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

            for (int i = 1; i <= 100; i++) {
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(
                        (int) (Math.random() * 100) + 1
                );
                em.persist(member);

                Order order = new Order();
                String city = "city" + i;
                String street = "street" + i;
                String zipcode = "zipcode" + i;

                order.setAddress(new Address(city, street, zipcode));
                order.setMember(member);
                order.setOrderAmount(
                        (int) (Math.random() * 20 + 1));
                em.persist(order);

                Team team = new Team();
                if (i >= 0 && i <= 25) {
                    team.setName("teamA");
                } else if (i >= 26 && i <= 50) {
                    team.setName("teamB");
                } else if (i >= 51 && i <= 75) {
                    team.setName("teamC");
                } else {
                    team.setName("temaD");
                }
                em.persist(team);
                member.setTeam(team);
            }

            em.flush();
            em.clear();

            List<Object[]> resultList = em.createQuery(
                    "select t.name, count(distinct m.age), avg(m.age), max(m.age) " +
                            "from Member m left outer join m.team t " +
                            "group by t.name " +
                            "order by t.name")
                    .getResultList();


            for (Object[] o : resultList) {
                String teamName = (String) o[0];
                Long countAge = (Long) o[1];
                Double avgAge = (Double) o[2];
                Integer maxAge = (Integer) o[3];

                System.out.println
                        (" result = teamName = " + teamName + ", countAge = " + countAge + ", avgAge = " + avgAge + ", maxAge = " + maxAge);
            }


            end = System.nanoTime();
            resultTime = end - start;

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("e.getLocalizedMessage() = " + e.getLocalizedMessage());
            tx.rollback();
        } finally {
            em.close();
            System.out.println("=========================");
            System.out.print("걸린 시간 : ");
            System.out.print((double) resultTime / 1000000000);
            System.out.println("초");

        }
        emf.close();


    }
}