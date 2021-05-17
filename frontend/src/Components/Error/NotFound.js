import React, {Component} from 'react';

export default class NotFound extends Component {
    constructor(props) {
        super(props);
        this.state = {
        };
    }
     componentDidMount() {
    }

    render() {
        return (
            <div>
                <h1>Not Found Error</h1>
                <p>Environment Does not exist</p>
            </div>
        )
    }
}