import React, {Component} from 'react';

export default class AddResources extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            environments: [],
            environmentName: '',
        };
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit = e => {
        e.preventDefault();
        let environmentName = this.environmentname.value;
        let path = `environment/${environmentName}`;
        this.props.history.push(path);
    }

    componentDidMount() {
    }

    render() {
        return(
            <div class="jumbotron">
                <h5>Capabilities > New Resources</h5>
                    <form className="form-inline" onSubmit={this.handleSubmit} method="POST">
                        <input type="text" className="form-control" placeholder="New Environment" ref={input => (this.environmentname = input)}></input>
                        <button className="btn primary" type="submit">Add</button>
                    </form>
            </div>
        )
    }
}