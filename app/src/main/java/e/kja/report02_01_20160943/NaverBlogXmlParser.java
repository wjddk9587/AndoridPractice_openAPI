package e.kja.report02_01_20160943;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class NaverBlogXmlParser { //블로그 openApiParser

    public enum TagType { NONE, TITLE, BLOGGERNAME , BLOGGERLINK};

    public ArrayList<NaverBlogDto> parse(String xml) {

        ArrayList<NaverBlogDto> resultList = new ArrayList();
        NaverBlogDto dto = null;

        NaverBlogXmlParser.TagType tagType = NaverBlogXmlParser.TagType.NONE;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("item")) {
                            dto = new NaverBlogDto();
                        } else if (parser.getName().equals("bloggername")) {
                            if (dto != null) tagType = NaverBlogXmlParser.TagType.BLOGGERNAME;
                        } else if (parser.getName().equals("bloggerlink")) {
                            if (dto != null) tagType = NaverBlogXmlParser.TagType. BLOGGERLINK;
                        } else if (parser.getName().equals("title")) {
                            if (dto != null) tagType = TagType.TITLE;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            resultList.add(dto);
                            dto = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case BLOGGERLINK:
                                dto.setBloggerlink(parser.getText());
                                break;
                            case BLOGGERNAME:
                                dto.setBloggername(parser.getText());
                                break;
                            case TITLE:
                                dto.setBlogtitle(parser.getText());
                                break;
                        }
                        tagType = NaverBlogXmlParser.TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
