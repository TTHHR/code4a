package cn.atd3.code4a.util;

/*

仅仅是用来替换c语言的头文件

create:harry 2018/2/13

 */
public class ReplaceCHeadInHtml {
    private static ReplaceCHeadInHtml rchih = null;

    private ReplaceCHeadInHtml() {
    }

    public static ReplaceCHeadInHtml getIns() {
        if (rchih == null)
            rchih = new ReplaceCHeadInHtml();
        return rchih;
    }

    public String getHtml(String html) {

        return findAndReplace(html);
    }

    private String findAndReplace(String html) {

        for (int i = 0; i <= html.lastIndexOf("#include"); i++) {
            i = html.indexOf("#include", i);
            int j = html.indexOf("<br>", i);
            String findString = html.substring(i, j);
            System.out.println("find head" + findString);
            html = html.replace(findString, findString.replace("<", "&lt;").replace(">", "&gt;"));
        }
        return html;
    }
}
