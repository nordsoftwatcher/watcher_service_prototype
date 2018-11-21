import React from 'react';
import styles from './Accordion.module.css';

export interface AccordionContext {
  openItemId: string | undefined;
  toggle: (id: string, isOpen: boolean) => void;
}

export const AccordionContext = React.createContext<AccordionContext>({
  openItemId: undefined,
  toggle: () => { /* noop */ },
});

export default class Accordion extends React.Component<{}, AccordionContext> {

  state = {
    openItemId: undefined,
    toggle: this.toggle.bind(this),
  };

  toggle(id: string, isOpen: boolean) {
    this.setState({
      openItemId: isOpen ? id : undefined,
    });
  }

  render() {
    return (
      <div className={styles.accordion}>
        <AccordionContext.Provider value={this.state}>
          {this.props.children}
        </AccordionContext.Provider>
      </div>
    );
  }
}
