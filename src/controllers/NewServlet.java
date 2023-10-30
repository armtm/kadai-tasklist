package controllers;

//新規登録処理（SQLでいうと、INSERT文）を行うコントローラ

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Task;
import utils.DBUtil;

/**
 * Servlet implementation class NewServlet
 */
@WebServlet("/new")
public class NewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub

        //①Task.java(modelクラス、エンティティクラス）をオブジェクト化
        EntityManager em = DBUtil.createEntityManager();
        em.getTransaction().begin();

        //②Task.javaのインスタンスを作成
        Task t = new Task();

        //③インスタンス t の各フィールドにデータを代入
        String content = "歯磨き";
        t.setContent(content);

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        t.setCreated_at(currentTime);
        t.setUpdated_at(currentTime);

        //④データベースに保存する
        em.persist(t);
        em.getTransaction().commit();

        //⑤自動採番されたIDの値を表示する
        response.getWriter().append(Integer.valueOf(t.getId()).toString());

        em.close();
    }

}
