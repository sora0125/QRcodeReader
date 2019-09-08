package jp.gr.java_conf.sora.qrcodereader

import android.content.Context
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import androidx.core.text.HtmlCompat


class HistoryAdapter(context: Context, private val historyList: List<HistoryData>):
        ArrayAdapter<HistoryData>(context, 0, historyList) {

    private val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    /**
     *  MethodName : getView
     *  Summary    : リストの項目のレイアウトを設定する
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // ViewHolderパターン
        var view = convertView!!
        var holder = HistoryViewHolder()

        view = layoutInflater.inflate(android.R.layout.simple_list_item_2, parent, false)
        holder.dateView = view?.findViewById(android.R.id.text1)
        holder.urlView = view?.findViewById(android.R.id.text2)
        view?.tag = holder


        // URLのリンク作成
        val mMethod = LinkMovementMethod.getInstance()
        holder.urlView?.movementMethod = mMethod
        val htmlUrl = "<a href=\"" + historyList[position].url + "\">" + historyList[position].url + "</a>"
        val link: CharSequence = fromHtml(htmlUrl)

        // 各項目に値をセットする
        holder.dateView?.text = historyList[position].date
        holder.urlView?.text = link
        // 自動的にリンクを貼るように設定
        holder.urlView?.autoLinkMask = Linkify.ALL

        return view
    }

    /**
     *  MethodName : fromHtml
     *  Summary    : htmlタグに変換する
     */
    private fun fromHtml(url: String): Spanned {
        val spanned: Spanned
        // VersionがNougat（API Level 24）以上か
        spanned = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(url, Html.FROM_HTML_MODE_LEGACY)
        } else {
            HtmlCompat.fromHtml(url, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
        return spanned
    }

}