package cn.granitech.util;

import cn.granitech.exception.ServiceException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

public class AssertHelper {
    public static void isNotNull(@Nullable Object object, String errorMessage) {
        if (object == null) {
            throw new ServiceException(errorMessage);
        }
    }

    public static void isNull(@Nullable Object object, String errorMessage) {
        if (object != null) {
            throw new ServiceException(errorMessage);
        }
    }

    public static void isTrue(Boolean expression, String errorMessage) {
        if (Boolean.FALSE.equals(expression)) {
            throw new ServiceException(errorMessage);
        }
    }

    public static void isNotEmpty(@Nullable Collection<?> collection, String errorMessage) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(errorMessage);
        }
    }

    public static void isEmpty(@Nullable Collection<?> collection, String errorMessage) {
        if (!CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(errorMessage);
        }
    }

    public static void isNotEmpty(@Nullable Object[] array, String errorMessage) {
        if (ObjectUtils.isEmpty(array)) {
            throw new ServiceException(errorMessage);
        }
    }

    public static void isNotBlank(String word, String errorMessage) {
        if (StringUtils.isBlank(word)) {
            throw new ServiceException(errorMessage);
        }
    }
}
