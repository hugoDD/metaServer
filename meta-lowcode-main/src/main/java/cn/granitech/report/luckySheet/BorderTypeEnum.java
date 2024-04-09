
package cn.granitech.report.luckySheet;

public enum BorderTypeEnum implements BaseCharEnum {
    BORDERNONE {
        public String getCode() {
            return "border-none";
        }

        public String getName() {
            return "BORDERNONE";
        }
    },
    BORDERALL {
        public String getCode() {
            return "border-all";
        }

        public String getName() {
            return "BORDERALL";
        }
    },
    BORDERLEFT {
        public String getCode() {
            return "border-left";
        }

        public String getName() {
            return "BORDERALL";
        }
    },
    BORDERRIGHT {
        public String getCode() {
            return "border-right";
        }

        public String getName() {
            return "BORDERRIGHT";
        }
    },
    BORDERTOP {
        public String getCode() {
            return "border-top";
        }

        public String getName() {
            return "BORDERTOP";
        }
    },
    BORDERBOTTOM {
        public String getCode() {
            return "border-bottom";
        }

        public String getName() {
            return "BORDERBOTTOM";
        }
    };

    private BorderTypeEnum() {
    }
}
