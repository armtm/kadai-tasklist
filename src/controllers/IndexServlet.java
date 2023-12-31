package controllers;

//一覧表示するコントローラー

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Task;
import utils.DBUtil;

/**
 * Servlet implementation class IndexServlet
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndexServlet() {
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

        //●開くページ数を取得（デフォルトを1ページ目にする）
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        }catch(NumberFormatException e) {}

        //②createNamedQueryメソッドの引数に、エンティティクラスで命名したgetALLTasksを指定し
        //データベースへ問い合わせる
        //③問い合わせ結果をリスト形式で取得（getResultList() メソッド）
        List<Task> tasks = em.createNamedQuery("getAllTasks", Task.class)

        //●最大件数と開始位置を指定してタスクデータを取得
                              .setFirstResult(15 * (page - 1))
                              .setMaxResults(15)
                              .getResultList();

        //●全件数を取得
        long tasks_count = (long)em.createNamedQuery("getTasksCount", Long.class)
                                     .getSingleResult();

        em.close();

        //④ビューである /WEB-INF/views/tasks/index.jsp を呼び出す
        //データベースから取得したタスク一覧（tasks）を、リクエストスコープにセットし、
        //index.jspを呼び出している
        request.setAttribute("tasks", tasks);

        //●全件数
        request.setAttribute("tasks_count", tasks_count);
        //●ページ数
        request.setAttribute("page", page);

        //フラッシュメッセージは１actionに１回表示とする。
        //フラッシュメッセージがセッションスコープにセットされている場合
        //リクエストスコープに保存する（セッションスコープからは削除する）
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/tasks/index.jsp");
        rd.forward(request, response);
    }

}
