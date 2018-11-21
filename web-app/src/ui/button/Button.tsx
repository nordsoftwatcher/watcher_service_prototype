import React from 'react';
import styles from './Button.module.css';

import cn from 'classnames';

export interface ButtonProps {
  solid?: boolean;
  outline?: boolean;
  link?: boolean;

  onClick?(): void;
}

export class Button extends React.Component<ButtonProps> {

  render() {
    return (
      <button
        className={cn({
          [styles.button]: true,
          [styles.solid]: this.props.solid,
          [styles.outline]: this.props.outline,
          [styles.link]: this.props.link,
        })}
        onClick={this.props.onClick}
      >
        {this.props.children}
      </button>
    );
  }
}
