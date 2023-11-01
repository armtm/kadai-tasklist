package controllers;

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
 * Servlet implementation class UpdateServlet
 */
@WebServlet("/update")
public class UpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateServlet() {
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

            //セッションスコープからタスクのIDを取得し、
            //該当のIDのタスク１件のみを、データベースから取得
            Task t = em.find(Task.class, (Integer)(request.getSession().getAttribute("task_id")));

            //フォームの内容を各フィールドに上書きする
            String content = request.getParameter("content");
            t.setContent(content);

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());

            //更新日時のみを上書きする
            t.setUpdated_at(currentTime);

            //●バリデーション機能（実行エラーがあれば編集画面のフォームに戻る）
            List<String> errors = TaskValidator.validate(t);
            if(errors.size() > 0) {
                em.close();

                //戻る際、フォームに初期値を設定し、タスクデータを送る
                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("task", t);
                request.setAttribute("errors", errors);

                RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/tasks/edit.jsp");
                rd.forward(request, response);
            }else {

            //データベースを更新
            em.getTransaction().begin();
            em.getTransaction().commit();
            //フラッシュメッセージをセッションスコープに保存し、index.jsp を呼び出す。
            request.getSession().setAttribute("flush", "更新が完了しました。");
            em.close();

            //セッションスコープ上の、不要になったデータ（更新前）を削除する
            request.getSession().removeAttribute("task_id");

            //index.jsp ページへリダイレクト
            response.sendRedirect(request.getContextPath() + "/index");
        }
    }
    }
}
