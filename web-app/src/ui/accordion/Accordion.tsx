import React from 'react';
import styles from './Accordion.module.css';

export interface AccordionContext {
  openItemId: string | undefined;
  toggle(id: string, isOpen: boolean): void;
}

export const AccordionContext = React.createContext<AccordionContext>({
  openItemId: undefined,
  toggle: () => { /* noop */ },
});

export interface AccordionProps {
  openItemId?: any;
}

export default class Accordion extends React.Component<AccordionProps, AccordionContext> {

  constructor(props: AccordionProps) {
    super(props);

    this.state = {
      openItemId: props.openItemId,
      toggle: this.toggle,
    };
  }

  componentDidUpdate(prevProps: AccordionProps) {
    if (prevProps.openItemId !== this.props.openItemId) {
      this.setState({
        openItemId: this.props.openItemId,
      });
    }
  }

  toggle = (id: string, isOpen: boolean) => {
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
