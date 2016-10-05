package org.zhaoqian.common.qualifier;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.inject.Stereotype;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
@Documented
@Stereotype
@Target({ TYPE })
@Retention(RUNTIME)
public @interface ViewModel
{

}
