package controllers;

//挿入処理を行うコントローラー

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Task;
import models.validators.TaskValidator;
import utils.DBUtil;


/**
 * Servlet implementation class CreateServlet
 */
@WebServlet("/create")
public class CreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub

        String _token = request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();
            em.getTransaction().begin();

            Task t = new Task();

            String content = request.getParameter("content");
            t.setContent(content);

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            t.setCreated_at(currentTime);
            t.setUpdated_at(currentTime);

            //●バリデーション機能（実行エラーがあれば新規登録フォームに戻る）
            List<String> errors = TaskValidator.validate(t);
            if(errors.size() > 0) {
                em.close();

                //戻る際、フォームに初期値を設定し、さらにタスクデータを送る
                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("task", t);
                request.setAttribute("errors", errors);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/tasks/new.jsp");
                rd.forward(request, response);
            } else {

                //データベースに保存
                em.persist(t);
                em.getTransaction().commit();
                request.getSession().setAttribute("flush", "登録が完了しました。");
                em.close();

                //indexのページにリダイレクト
                response.sendRedirect(request.getContextPath() + "/index");
            }

/*
            //データベースに保存
            em.persist(t);
            em.getTransaction().commit();
            //フラッシュメッセージをセッションスコープに保存し、index.jsp を呼び出す。
            request.getSession().setAttribute("flush", "登録が完了しました。");
            em.close();

            //indexのページにリダイレクト
            response.sendRedirect(request.getContextPath() + "/index");
            */
        }
    }
}
