import React from 'react';
import styles from './Button.module.css';

export interface ButtonProps {
  solid?: boolean;
  outline?: boolean;
  link?: boolean;

  onClick?(): void;
}

export class Button extends React.Component<ButtonProps> {

  getClassName() {
    const result = [styles.button];
    this.props.solid && result.push(styles.solid);
    this.props.outline && result.push(styles.outline);
    this.props.link && result.push(styles.link);
    return result.join(' ');
  }

  render() {
    return (
      <button className={this.getClassName()} onClick={this.props.onClick}>
        {this.props.children}
      </button>
    )
  }
}