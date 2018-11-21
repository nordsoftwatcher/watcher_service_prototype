import React from 'react';
import styles from './Panel.module.css';

export interface PanelProps {
  label: string;
}

export class Panel extends React.Component<PanelProps> {

  render() {
    return (
      <div className={styles.panel}>
        <div className={styles.header}>
          {this.props.label}
        </div>
        <div className={styles.body}>
          {this.props.children}
        </div>
      </div>
    );
  }
}
