
package cn.granitech.report.luckySheet;

public enum LuckySheetPropsEnum implements BaseCharEnum {
    R {
        public String getCode() {
            return "r";
        }

        public String getName() {
            return "单元格横坐标";
        }
    },
    C {
        public String getCode() {
            return "c";
        }

        public String getName() {
            return "单元格纵坐标";
        }
    },
    CELLCONFIG {
        public String getCode() {
            return "v";
        }

        public String getName() {
            return "单元格配置";
        }
    },
    CELLVALUE {
        public String getCode() {
            return "v";
        }

        public String getName() {
            return "单元格值";
        }
    },
    CELLVALUEM {
        public String getCode() {
            return "m";
        }

        public String getName() {
            return "单元格值";
        }
    },
    COORDINATECONNECTOR {
        public String getCode() {
            return "_";
        }

        public String getName() {
            return "坐标连接符";
        }
    },
    CELLEXTEND {
        public String getCode() {
            return "cellExtend";
        }

        public String getName() {
            return "扩展方向";
        }
    },
    AGGREGATETYPE {
        public String getCode() {
            return "aggregateType";
        }

        public String getName() {
            return "聚合方式";
        }
    },
    CELLFUNCTION {
        public String getCode() {
            return "cellFunction";
        }

        public String getName() {
            return "汇总方式";
        }
    },
    DIGIT {
        public String getCode() {
            return "digit";
        }

        public String getName() {
            return "小数位数";
        }
    },
    GROUPSUMMARYDEPENDENCYR {
        public String getCode() {
            return "groupSummaryDependencyr";
        }

        public String getName() {
            return "分组依赖行号";
        }
    },
    GROUPSUMMARYDEPENDENCYC {
        public String getCode() {
            return "groupSummaryDependencyc";
        }

        public String getName() {
            return "分组依赖列号";
        }
    },
    MERGECELLS {
        public String getCode() {
            return "mc";
        }

        public String getName() {
            return "合并单元格";
        }
    },
    ROWSPAN {
        public String getCode() {
            return "rs";
        }

        public String getName() {
            return "合并行数";
        }
    },
    COLSPAN {
        public String getCode() {
            return "cs";
        }

        public String getName() {
            return "合并列数";
        }
    },
    FUNCTION {
        public String getCode() {
            return "f";
        }

        public String getName() {
            return "函数配置";
        }
    },
    LINKADDRESS {
        public String getCode() {
            return "linkAddress";
        }

        public String getName() {
            return "超链接";
        }
    },
    LINKTYPE {
        public String getCode() {
            return "linkType";
        }

        public String getName() {
            return "超链接类型";
        }
    },
    LINKTOOLTIP {
        public String getCode() {
            return "linkTooltip";
        }

        public String getName() {
            return "超链接提示";
        }
    },
    ROWLEN {
        public String getCode() {
            return "rowlen";
        }

        public String getName() {
            return "行高";
        }
    },
    COLUMNLEN {
        public String getCode() {
            return "columnlen";
        }

        public String getName() {
            return "列宽";
        }
    },
    MERGECONFIG {
        public String getCode() {
            return "merge";
        }

        public String getName() {
            return "合并单元格配置";
        }
    },
    BACKGROUND {
        public String getCode() {
            return "bg";
        }

        public String getName() {
            return "单元格背景颜色";
        }
    },
    FONTFAMILY {
        public String getCode() {
            return "ff";
        }

        public String getName() {
            return "单元格字体";
        }
    },
    FONTCOLOR {
        public String getCode() {
            return "fc";
        }

        public String getName() {
            return "单元格字体颜色";
        }
    },
    FONTSIZE {
        public String getCode() {
            return "fs";
        }

        public String getName() {
            return "单元格字体大小";
        }
    },
    BOLD {
        public String getCode() {
            return "bl";
        }

        public String getName() {
            return "单元格字体加粗";
        }
    },
    ITALIC {
        public String getCode() {
            return "it";
        }

        public String getName() {
            return "单元格字体斜体";
        }
    },
    CANCELLINE {
        public String getCode() {
            return "cl";
        }

        public String getName() {
            return "单元格字体删除线";
        }
    },
    UNDERLINE {
        public String getCode() {
            return "un";
        }

        public String getName() {
            return "单元格字体下划线";
        }
    },
    HORIZONTALTYPE {
        public String getCode() {
            return "ht";
        }

        public String getName() {
            return "水平对齐";
        }
    },
    VERTICALTYPE {
        public String getCode() {
            return "vt";
        }

        public String getName() {
            return "垂直对齐";
        }
    },
    DATAFROM {
        public String getCode() {
            return "dataFrom";
        }

        public String getName() {
            return "数据来源";
        }
    },
    ISGROUPMERGE {
        public String getCode() {
            return "isGroupMerge";
        }

        public String getName() {
            return "分组单元格是否合一";
        }
    },
    CELLTYPE {
        public String getCode() {
            return "ct";
        }

        public String getName() {
            return "单元格格式";
        }
    },
    TYPE {
        public String getCode() {
            return "t";
        }

        public String getName() {
            return "单元格值格式";
        }
    },
    INLINESTR {
        public String getCode() {
            return "inlineStr";
        }

        public String getName() {
            return "单元格值格式内联字符串";
        }
    },
    STRING {
        public String getCode() {
            return "s";
        }

        public String getName() {
            return "单元格值格式纯字符串";
        }
    },
    BORDERINFO {
        public String getCode() {
            return "borderInfo";
        }

        public String getName() {
            return "边框信息";
        }
    },
    BORDERRANGE {
        public String getCode() {
            return "range";
        }

        public String getName() {
            return "边框范围";
        }
    },
    BORDERCOLUMNRANGE {
        public String getCode() {
            return "column";
        }

        public String getName() {
            return "边框列范围";
        }
    },
    BORDERROWRANGE {
        public String getCode() {
            return "row";
        }

        public String getName() {
            return "边框行范围";
        }
    },
    BORDERTYPE {
        public String getCode() {
            return "borderType";
        }

        public String getName() {
            return "边框类型";
        }
    },
    RANGETYPE {
        public String getCode() {
            return "rangeType";
        }

        public String getName() {
            return "范围类型";
        }
    },
    RANGETYPECELL {
        public String getCode() {
            return "cell";
        }

        public String getName() {
            return "单元格类型";
        }
    },
    RANGECOLOR {
        public String getCode() {
            return "color";
        }

        public String getName() {
            return "边框颜色";
        }
    },
    RANGESTYLE {
        public String getCode() {
            return "style";
        }

        public String getName() {
            return "边框格式";
        }
    },
    TEXTWRAPMODE {
        public String getCode() {
            return "tb";
        }

        public String getName() {
            return "换行方式";
        }
    },
    RANGECELLVALUE {
        public String getCode() {
            return "value";
        }

        public String getName() {
            return "单元格边框类型值";
        }
    };

    LuckySheetPropsEnum() {
    }
}
