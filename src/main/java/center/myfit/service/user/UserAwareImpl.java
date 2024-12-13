package center.myfit.service.user;

import center.myfit.starter.service.UserAware;
import org.springframework.stereotype.Service;

/** Имплементация UserAware. */
@Service
public class UserAwareImpl implements UserAware<Object> {
  @Override
  public Object getUser() {
    // TODO подумать как реализовать и надо ли
    throw new UnsupportedOperationException();
  }
}
